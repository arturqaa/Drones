package com.artur.service.dto;

import com.artur.types.CategoryType;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Data
public class ProductPhotoDto {
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
    private byte[] photo;
}
