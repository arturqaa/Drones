package com.artur.service.dto;

import com.artur.types.StatusType;
import com.artur.valid.ValidPhoneNumber;
import lombok.Data;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.Set;

@Data
@ValidPhoneNumber
public class UserOrderDto {
    @NotNull
    @NotEmpty
    private Long id;
    @NotNull
    @NotEmpty
    private Long accountId;
    @NotNull
    @NotEmpty
    private Long amount;

    private StatusType status;
    private List<ProductPhotoDto> products;
    private String phoneNumber;
    private String address;
    private int totalElements;
}
