package com.artur.service.mapper;

import com.artur.entity.UserOrder;
import com.artur.service.dto.UserOrderDto;
import com.artur.utils.InstantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {InstantMapper.class, AccountMapper.class})
public interface UserOrderMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "products", ignore = true)
    UserOrder toEntity(UserOrderDto dto);

    @Mapping(target = "products", source = "products", ignore = true)
    @Mapping(target = "status", source = "status.statusName")
    @Mapping(target = "accountId", source = "account.id")
    UserOrderDto toDto(UserOrder entity);
}
