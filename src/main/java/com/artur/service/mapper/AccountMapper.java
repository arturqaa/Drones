package com.artur.service.mapper;

import com.artur.entity.Account;
import com.artur.service.dto.AccountDto;
import com.artur.utils.InstantMapper;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(uses = {InstantMapper.class})
public interface AccountMapper {
    @Mapping(target = "id", ignore = true)
    Account toEntity(AccountDto dto);

    @Mapping(target = "password", ignore = true)
    @Mapping(target = "matchingPassword", ignore = true)
    @Mapping(target = "role", source = "role.roleName")
    AccountDto toDto(Account entity);
}
