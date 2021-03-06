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
import com.artur.service.dto.ProductDto;
import com.artur.service.dto.ProductPhotoDto;
import com.artur.service.dto.UserOrderDto;
import com.artur.service.mapper.ProductPhotoMapper;
import com.artur.service.mapper.UserOrderMapper;
import com.artur.exception.ResourceNotFoundException;
import com.artur.service.mapper.ProductMapper;
import com.artur.types.StatusType;
import com.artur.utils.FileDownlandUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;


import java.io.IOException;
import java.util.*;

@Slf4j
@Service
public class UserOrderServiceImpl implements UserOrderService {

    private final UserOrderMapper userOrderMapper;
    private final UserOrderRepository userOrderRepository;
    private final AccountRepository accountRepository;
    private final StatusRepository statusRepository;
    private final ProductPhotoMapper productPhotoMapper;
    private static final Long ZERO = 0L;

    public UserOrderServiceImpl(UserOrderMapper userOrderMapper, UserOrderRepository userOrderRepository, AccountRepository accountRepository, StatusRepository statusRepository, ProductPhotoMapper productPhotoMapper) {
        this.userOrderMapper = userOrderMapper;
        this.userOrderRepository = userOrderRepository;
        this.accountRepository = accountRepository;
        this.statusRepository = statusRepository;
        this.productPhotoMapper = productPhotoMapper;
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
    public Page<UserOrderDto> getAllOrders(Pageable pageable) {
        Status statusEntity = statusRepository.findByStatusName(StatusType.ACCEPTED).orElseThrow(
                () -> new ResourceNotFoundException("Status not found."));
        Page<UserOrder> ordersPage = userOrderRepository.findAllByStatus(statusEntity, pageable);
        return ordersPage.map(userOrderMapper::toDto);
    }

    @Override
    public Page<UserOrderDto> getAllOrdersByUserId(Long userId, OrderSpecification orderSpecification, Pageable pageable) {
        orderSpecification.setAccountId(userId);
        Page<UserOrder> ordersPage = userOrderRepository.findAll(orderSpecification, pageable);
//        for(UserOrder userOrder : ordersPage){
//            Set<Product> products = userOrder.getProducts();
//            for(Product product: products){
//                productMapper.toDto(product);
//            }
//        }
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

    public UserOrderDto getActiveOrderForUser(Long userId) throws IOException {
        Status statusEntity = statusRepository.findByStatusName(StatusType.ACTIVE).orElseThrow(
                () -> new ResourceNotFoundException("Status order not found."));
        Account account = accountRepository.findById(userId).get();
        UserOrder order = userOrderRepository.findAllByStatusAndAccount(statusEntity, account).get();
        UserOrderDto userOrderDto = userOrderMapper.toDto(order);
        List<ProductPhotoDto> productPhotoDtos = new ArrayList<>();
        for(Product product: order.getProducts()){
            ProductPhotoDto productPhotoDto = productPhotoMapper.toDto(product);
            productPhotoDto.setPhoto(FileDownlandUtil.addFileToDto(product.getPhotoPath()));
            productPhotoDtos.add(productPhotoDto);
        }
        userOrderDto.setProducts(productPhotoDtos);
        userOrderDto.setTotalElements(userOrderDto.getProducts().size());
        return userOrderDto;
    }

    public void confirmActiveOrderForUser(UserOrderDto userOrderDto){
         UserOrder userOrderEntity = userOrderRepository.findById(userOrderDto.getId()).get();
        Status statusEntity = statusRepository.findByStatusName(StatusType.ACCEPTED).orElseThrow(
                () -> new ResourceNotFoundException("Status not found."));
        userOrderEntity.setStatus(statusEntity);
        userOrderEntity.setAddress(userOrderDto.getAddress());
        userOrderEntity.setPhoneNumber(userOrderDto.getPhoneNumber());
        createOrderForUser(userOrderEntity.getAccount());
    }

}
