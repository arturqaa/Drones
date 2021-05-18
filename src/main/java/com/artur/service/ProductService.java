package com.artur.service;

import com.artur.service.dto.ProductDto;
import com.artur.service.dto.ProductPhotoDto;
import com.artur.types.CategoryType;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface ProductService {

    List<ProductPhotoDto> getAllProductsByCategory(CategoryType categoryType, Pageable pageable) throws IOException;

    List<ProductPhotoDto> getAllProductsByCategoryDrones(Pageable pageable) throws IOException;

    List<ProductPhotoDto> getAllProductsByCategoryRacerDrones(Pageable pageable) throws IOException;

    List<ProductPhotoDto> getAllProductsByCategoryActionCameras(Pageable pageable) throws IOException;

    List<ProductPhotoDto> getAllProductsByCategoryAccessories(Pageable pageable) throws IOException;

    Page<ProductDto> getAllProductsByTitle(String title, Pageable pageable);

    Long createProduct(ProductDto productDto);

    void addProductInOrder(Long userId, Long productId);

    void deleteProductFromOrder(Long userId, Long productId);

    void addPictureInProduct(Long productId, MultipartFile photo) throws IOException;

    List<ProductPhotoDto> getAllProducts(Pageable pageable) throws IOException;
}
