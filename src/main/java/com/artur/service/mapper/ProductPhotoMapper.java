package com.artur.service.mapper;

import com.artur.entity.Product;
import com.artur.service.dto.ProductPhotoDto;
import com.artur.utils.InstantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {InstantMapper.class, AccountMapper.class})
public interface ProductPhotoMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userOrders", ignore = true)
    Product toEntity(ProductPhotoDto dto);

    @Mapping(target = "category", source = "category.categoryName")
    @Mapping(target = "photo", ignore = true)
    ProductPhotoDto toDto(Product entity);
}
