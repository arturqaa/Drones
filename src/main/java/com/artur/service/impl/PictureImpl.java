package com.artur.service.impl;

import com.artur.entity.Picture;
import com.artur.repo.PictureRepository;
import com.artur.service.PictureService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.Optional;
import java.util.zip.DataFormatException;
import java.util.zip.Deflater;
import java.util.zip.Inflater;

@Slf4j
@Service
public class PictureImpl implements PictureService {

    PictureRepository pictureRepository;

    @Override
    public Picture uploadPicture(MultipartFile file, Pageable pageable) throws IOException {
        Picture picture = new Picture(file.getOriginalFilename(), file.getContentType(),
                compressBytes(file.getBytes()));
        pictureRepository.save(picture);
        return picture;
    }

    @Override
    public Picture getPictureByName(String name, Pageable pageable) throws IOException {
        Optional<Picture> retrievedImage = pictureRepository.findByName(name);
        return new Picture(retrievedImage.get().getName(),
                retrievedImage.get().getType(),
                decompressBytes(retrievedImage.get().getPicByte()));
    }


    // compress the image bytes before storing it in the database
    private static byte[] compressBytes(byte[] data) {
        Deflater deflater = new Deflater();
        deflater.setInput(data);
        deflater.finish();
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        while (!deflater.finished()) {
            int count = deflater.deflate(buffer);
            outputStream.write(buffer, 0, count);
        }
        try {
            outputStream.close();
        } catch (IOException e) {
        }
        System.out.println("Compressed Image Byte Size - " + outputStream.toByteArray().length);
        return outputStream.toByteArray();
    }

    // uncompress the image bytes before returning it to the angular application
    public static byte[] decompressBytes(byte[] data) {
        Inflater inflater = new Inflater();
        inflater.setInput(data);
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream(data.length);
        byte[] buffer = new byte[1024];
        try {
            while (!inflater.finished()) {
                int count = inflater.inflate(buffer);
                outputStream.write(buffer, 0, count);
            }
            outputStream.close();
        } catch (IOException | DataFormatException ignored) {
        }
        return outputStream.toByteArray();
    }
}
