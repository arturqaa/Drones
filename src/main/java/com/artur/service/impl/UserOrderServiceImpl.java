package com.artur.service.impl;

import com.artur.entity.Account;
import com.artur.entity.UserOrder;
import com.artur.entity.Product;
import com.artur.entity.Status;
import com.artur.repo.UserOrderRepository;
import com.artur.repo.StatusRepository;
import com.artur.repo.AccountRepository;
import com.artur.repo.specification.OrderSpecification;
import com.artur.service.UserOrderService;
import com.artur.service.dto.UserOrderDto;
import com.artur.service.mapper.UserOrderMapper;
import com.artur.exception.ResourceNotFoundException;
import com.artur.service.mapper.ProductMapper;
import com.artur.types.StatusType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class UserOrderServiceImpl implements UserOrderService {

    private final UserOrderMapper userOrderMapper;
    private final UserOrderRepository userOrderRepository;
    private final AccountRepository accountRepository;
    private final StatusRepository statusRepository;
    private final ProductMapper productMapper;
    private static final Long ZERO = 0L;

    public UserOrderServiceImpl(UserOrderMapper userOrderMapper, UserOrderRepository userOrderRepository, AccountRepository accountRepository, StatusRepository statusRepository, ProductMapper productMapper) {
        this.userOrderMapper = userOrderMapper;
        this.userOrderRepository = userOrderRepository;
        this.accountRepository = accountRepository;
        this.statusRepository = statusRepository;
        this.productMapper = productMapper;
    }

    @Override
    public UserOrderDto crateOrderByUserId(Long id, UserOrderDto userOrderDto) {
        UserOrder userOrderEntity = userOrderMapper.toEntity(userOrderDto);
        Account accountEntity = accountRepository.findById(id).orElseThrow(
                () -> new ResourceNotFoundException("Account", "id", id));
        userOrderEntity.setAccount(accountEntity);
        return userOrderMapper.toDto(userOrderRepository.save(userOrderEntity));
    }

    @Override
    public Page<UserOrderDto> getAllOrders(OrderSpecification orderSpecification, Pageable pageable) {
        Page<UserOrder> ordersPage = userOrderRepository.findAll(orderSpecification, pageable);
        return ordersPage.map(userOrderMapper::toDto);
    }

    @Override
    public Page<UserOrderDto> getAllOrdersByUserId(Long userId, OrderSpecification orderSpecification, Pageable pageable) {
        orderSpecification.setAccountId(userId);
        Page<UserOrder> ordersPage = userOrderRepository.findAll(orderSpecification, pageable);
        for(UserOrder userOrder : ordersPage){
            Set<Product> products = userOrder.getProducts();
            for(Product product: products){
                productMapper.toDto(product);
            }
        }
        return ordersPage.map(userOrderMapper::toDto);
    }

    @Override
    public void createOrderForUser(Account account) {
        UserOrder userOrderEntity = new UserOrder(account);
        Status statusEntity = statusRepository.findByStatusName(StatusType.ACTIVE).orElseThrow(
                () -> new ResourceNotFoundException("Status not found."));
        userOrderEntity.setStatus(statusEntity);
        userOrderEntity.setAmount(ZERO);
        userOrderRepository.save(userOrderEntity);
    }

    public UserOrderDto getActiveOrderForUser(Long userId){
        Status statusEntity = statusRepository.findByStatusName(StatusType.ACTIVE).orElseThrow(
                () -> new ResourceNotFoundException("Status order not found."));
        Optional<UserOrder> orders = userOrderRepository.findAllByStatusAndAccountId(statusEntity, userId);
        return userOrderMapper.toDto(orders.get());
    }
}
