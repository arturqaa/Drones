package com.artur.service;

import com.artur.entity.Account;
import com.artur.repo.specification.OrderSpecification;
import com.artur.service.dto.UserOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.io.IOException;

public interface UserOrderService {
    UserOrderDto crateOrderByUserId(Long id, UserOrderDto userOrderDto);

    Page<UserOrderDto> getAllOrders(Pageable pageable);

    Page<UserOrderDto> getAllOrdersByUserId(Long userId, OrderSpecification orderSpecification, Pageable pageable);

    UserOrderDto getActiveOrderForUser(Long userId) throws IOException;

    void createOrderForUser(Account account);

    void confirmActiveOrderForUser(UserOrderDto userOrderDto);
}
