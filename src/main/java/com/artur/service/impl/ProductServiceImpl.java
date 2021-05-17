package com.artur.service.impl;

import com.artur.entity.*;
import com.artur.exception.ResourceNotFoundException;
import com.artur.repo.CategoryRepository;
import com.artur.repo.UserOrderRepository;
import com.artur.repo.ProductRepository;
import com.artur.repo.StatusRepository;
import com.artur.service.ProductService;
import com.artur.service.dto.ProductDto;
import com.artur.service.dto.ProductPhotoDto;
import com.artur.service.mapper.ProductMapper;
import com.artur.service.mapper.ProductPhotoMapper;
import com.artur.types.StatusType;
import com.artur.utils.FileDownlandUtil;
import com.artur.utils.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import com.artur.types.CategoryType;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
public class ProductServiceImpl implements ProductService {
    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final UserOrderRepository userOrderRepository;
    private final StatusRepository statusRepository;
    private final CategoryRepository categoryRepository;
    private final ProductPhotoMapper productPhotoMapper;

    public ProductServiceImpl(ProductRepository productRepository, ProductMapper productMapper, UserOrderRepository userOrderRepository, StatusRepository statusRepository, CategoryRepository categoryRepository, ProductPhotoMapper productPhotoMapper) {
        this.productRepository = productRepository;
        this.productMapper = productMapper;
        this.userOrderRepository = userOrderRepository;
        this.statusRepository = statusRepository;
        this.categoryRepository = categoryRepository;
        this.productPhotoMapper = productPhotoMapper;
    }


    @Override
    public List<ProductPhotoDto> getAllProductsByCategory(CategoryType categoryType, Pageable pageable) throws IOException {
        Category categoryEntity = categoryRepository.findByCategoryName(categoryType).orElseThrow(
                () -> new ResourceNotFoundException("Status order not found."));
        Page<Product> productPage = productRepository.findAllByCategory(categoryEntity, pageable);
        return pageProductsToDto(productPage);
    }

    @Override
    public List<ProductPhotoDto> getAllProductsByCategoryDrones(Pageable pageable) throws IOException {
        return getAllProductsByCategory(CategoryType.DRONES, pageable);
    }

    @Override
    public List<ProductPhotoDto> getAllProductsByCategoryActionCameras(Pageable pageable) throws IOException {
        return getAllProductsByCategory(CategoryType.ACTION_CAMERAS, pageable);
    }

    @Override
    public List<ProductPhotoDto> getAllProductsByCategoryAccessories(Pageable pageable) throws IOException {
        return getAllProductsByCategory(CategoryType.ACCESSORIES, pageable);
    }

    @Override
    public List<ProductPhotoDto> getAllProductsByCategoryRacerDrones(Pageable pageable) throws IOException {
        return getAllProductsByCategory(CategoryType.RACER_DRONES, pageable);
    }

    @Override
    public Page<ProductDto> getAllProductsByTitle(String title, Pageable pageable) {
        Page<Product> productPage = productRepository.findAllByTitle(title, pageable);
        return productPage.map(productMapper::toDto);
    }

    public Long createProduct(ProductDto productDto) {
        Product productEntity = productMapper.toEntity(productDto);
        CategoryType category = productDto.getCategory() == null ? CategoryType.DRONES : productDto.getCategory();
        Category categoryEntity = categoryRepository.findByCategoryName
                (category).orElseThrow(
                () -> new ResourceNotFoundException("Category not found."));
        productEntity.setCategory(categoryEntity);
        productMapper.toDto(productRepository.save(productEntity));
        return productEntity.getId();
    }

    public void addPictureInProduct(Long productId, MultipartFile photo) throws IOException {
        Product productEntity = productRepository.findById(productId).get();
        String uploadDir = null;
        if (photo != null){
            uploadDir = "ad-photos/" + productEntity.getTitle();
        }
        if (photo != null){
            productEntity.setPhotoPath(FileUploadUtil.saveFile(uploadDir, photo));
        }
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

    public List<ProductPhotoDto> getAllProducts(Pageable pageable) throws IOException {
        Page<Product> pageProducts = productRepository.findAll(pageable);
        return pageProductsToDto(pageProducts);
    }

    public List<ProductPhotoDto> pageProductsToDto(Page<Product> pageProducts) throws IOException {
        List<ProductPhotoDto> productPhotoDtos = new ArrayList<>();
        for (Product product: pageProducts){
            ProductPhotoDto productPhotoDto = productPhotoMapper.toDto(product);
            productPhotoDto.setPhoto(FileDownlandUtil.addFileToDto(product.getPhotoPath()));
            productPhotoDtos.add(productPhotoDto);
        }
        return productPhotoDtos;
    }
}
