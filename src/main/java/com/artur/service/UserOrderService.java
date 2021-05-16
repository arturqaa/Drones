package com.artur.service;

import com.artur.entity.Account;
import com.artur.repo.specification.OrderSpecification;
import com.artur.service.dto.UserOrderDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserOrderService {
    UserOrderDto crateOrderByUserId(Long id, UserOrderDto userOrderDto);

    Page<UserOrderDto> getAllOrders(OrderSpecification orderSpecification, Pageable pageable);

    Page<UserOrderDto> getAllOrdersByUserId(Long userId, OrderSpecification orderSpecification, Pageable pageable);

    UserOrderDto getActiveOrderForUser(Long userId, OrderSpecification orderSpecification, Pageable pageable);

    void createOrderForUser(Account account);
}
