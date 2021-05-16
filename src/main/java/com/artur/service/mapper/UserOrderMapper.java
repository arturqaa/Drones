package com.artur.service.mapper;

import com.artur.entity.UserOrder;
import com.artur.service.dto.UserOrderDto;
import com.artur.utils.InstantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.HashSet;
import java.util.Set;

@Mapper(uses = {InstantMapper.class, AccountMapper.class, CategoryMapper.class, ProductMapper.class})
public interface UserOrderMapper {

    //ProductMapperImpl productMapper = new ProductMapperImpl();

    @Mapping(target = "id", ignore = true)
    UserOrder toEntity(UserOrderDto dto);

    @Mapping(target = "totalElements", ignore = true)
    @Mapping(target = "products", source = "products")
    @Mapping(target = "status", source = "status.statusName")
    @Mapping(target = "accountId", source = "account.id")
    UserOrderDto toDto(UserOrder entity);
}
