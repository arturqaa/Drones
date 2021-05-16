package com.artur.service;

import com.artur.service.dto.ProductDto;
import com.artur.types.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductDto> getAllProducts(Pageable pageable);

    Page<ProductDto> getAllProductsByCategory(CategoryType categoryType, Pageable pageable);

    Page<ProductDto> getAllProductsByCategoryDrones(Pageable pageable);

    Page<ProductDto> getAllProductsByCategoryRacerDrones(Pageable pageable);

    Page<ProductDto> getAllProductsByCategoryActionCameras(Pageable pageable);

    Page<ProductDto> getAllProductsByCategoryAccessories(Pageable pageable);

    Page<ProductDto> getAllProductsByTitle(String title, Pageable pageable);

    void createProduct(ProductDto productDto);

    void addProductInOrder(Long userId, Long productId);

    void deleteProductFromOrder(Long userId, Long productId);
}
