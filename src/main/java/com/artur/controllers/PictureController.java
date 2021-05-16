package com.artur.controllers;

import com.artur.entity.Picture;
import com.artur.service.PictureService;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@RestController
public class PictureController {
    private final PictureService pictureService;

    public PictureController(PictureService pictureService) {
        this.pictureService = pictureService;
    }

    @PostMapping("/upload_pic")
    public ResponseEntity<Picture> uploadPicture(@RequestParam("imageFile") MultipartFile file, Pageable pageable) throws IOException {
        return ResponseEntity.ok().body(pictureService.uploadPicture(file, pageable));
    }
}
