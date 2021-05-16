package com.artur.service.mapper;

import com.artur.entity.Product;
import com.artur.service.dto.ProductDto;
import com.artur.utils.InstantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.MappingConstants;
import org.mapstruct.ValueMapping;

@Mapper(uses = {InstantMapper.class})
public interface ProductMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "userOrders", ignore = true)
    Product toEntity(ProductDto dto);

    @Mapping(target = "category", source = "category.categoryName")
    ProductDto toDto(Product entity);
}
