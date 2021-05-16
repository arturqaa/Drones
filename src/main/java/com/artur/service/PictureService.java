package com.artur.service;

import com.artur.entity.Picture;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface PictureService {

    Picture uploadPicture(MultipartFile file , Pageable pageable) throws IOException;

    Picture getPictureByName(String name , Pageable pageable) throws IOException;
}
