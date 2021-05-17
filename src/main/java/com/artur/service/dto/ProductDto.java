package com.artur.service.dto;

import com.artur.entity.Picture;
import com.artur.types.CategoryType;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProductDto {
    @NotNull
    @NotEmpty
    private Long id;
    @NotNull
    @NotEmpty
    private String title;
    private String description;
    @NotNull
    @NotEmpty
    private Long price;
    private CategoryType category;
    private Picture picture;
    private MultipartFile photo;
}
