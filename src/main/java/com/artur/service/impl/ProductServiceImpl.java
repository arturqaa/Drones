package com.artur.service.impl;

import com.artur.entity.*;
import com.artur.exception.ResourceNotFoundException;
import com.artur.repo.CategoryRepository;
import com.artur.repo.UserOrderRepository;
import com.artur.repo.ProductRepository;
import com.artur.repo.StatusRepository;
import com.artur.service.ProductService;
import com.artur.service.dto.ProductDto;
import com.artur.service.mapper.ProductMapper;
import com.artur.types.RoleType;
import com.artur.types.StatusType;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import com.artur.types.CategoryType;
import org.springframework.stereotype.Service;

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserOrderRepository userOrderRepository;
    private final StatusRepository statusRepository;
    private final CategoryRepository categoryRepository;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, UserOrderRepository userOrderRepository, StatusRepository statusRepository, CategoryRepository categoryRepository) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userOrderRepository = userOrderRepository;
        this.statusRepository = statusRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<ProductDto> getAllProducts(Pageable pageable) {
        Page<Product> productPage = productRepository.findAll(pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> getAllProductsByCategory(CategoryType categoryType, Pageable pageable) {
        Category categoryEntity = categoryRepository.findByCategoryName(categoryType).orElseThrow(
                () -> new ResourceNotFoundException("Status order not found."));
        Page<Product> productPage = productRepository.findAllByCategory(categoryEntity, pageable);
        return productPage.map(productMapper::toDto);
    }

    @Override
    public Page<ProductDto> getAllProductsByCategoryDrones(Pageable pageable) {
        return getAllProductsByCategory(CategoryType.DRONES, pageable);
    }

    @Override
    public Page<ProductDto> getAllProductsByCategoryActionCameras(Pageable pageable) {
        return getAllProductsByCategory(CategoryType.ACTION_CAMERAS, pageable);
    }

    @Override
    public Page<ProductDto> getAllProductsByCategoryAccessories(Pageable pageable) {
        return getAllProductsByCategory(CategoryType.ACCESSORIES, pageable);
    }

    @Override
    public Page<ProductDto> getAllProductsByCategoryRacerDrones(Pageable pageable) {
        return getAllProductsByCategory(CategoryType.RACER_DRONES, pageable);
    }

    @Override
    public Page<ProductDto> getAllProductsByTitle(String title, Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByTitle(title, pageable);
        return productPage.map(productMapper::toDto);
    }

    public void createProduct(ProductDto productDto) {
        Product productEntity = productMapper.toEntity(productDto);
        CategoryType category = productDto.getCategory() == null ? CategoryType.DRONES : productDto.getCategory();
        Category categoryEntity = categoryRepository.findByCategoryName
                (category).orElseThrow(
                () -> new ResourceNotFoundException("Category not found."));
        productEntity.setCategory(categoryEntity);
        productMapper.toDto(productRepository.save(productEntity));
    }

    @Override
    public void addProductInOrder(Long userId, Long productId) {
        UserOrder userOrder = getActiveOrderByUserId(userId);
        Product product = productRepository.findById(productId).get();
        userOrder.setAmount(userOrder.getAmount() + product.getPrice());
        userOrder.addProduct(product);
        userOrderRepository.save(userOrder);
    }

    @Override
    public void deleteProductFromOrder(Long userId, Long productId) {
        UserOrder userOrder = getActiveOrderByUserId(userId);
        Product product = productRepository.findById(productId).get();
        userOrder.setAmount(userOrder.getAmount() - product.getPrice());
        userOrder.deleteProduct(product);
        userOrderRepository.save(userOrder);
    }

    public UserOrder getActiveOrderByUserId(Long userId){
        Optional<UserOrder> ordersByUserId = userOrderRepository.findAllByAccountId(userId);
        Status statusEntity = statusRepository.findByStatusName(StatusType.ACTIVE).orElseThrow(
                () -> new ResourceNotFoundException("Status order not found."));
        return ordersByUserId.stream().filter(order -> (order.getStatus()).equals(statusEntity)).findFirst().get();
    }

}
