package com.artur.controllers;

import com.artur.service.ProductService;
import com.artur.service.dto.ProductDto;
import com.artur.service.dto.ProductPhotoDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping(value = "/drones", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<List<ProductPhotoDto>> getAllProductsByCategoryDrones(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        return ResponseEntity.ok().body(productService.getAllProductsByCategoryDrones(pageable));
    }

    @GetMapping(value = "/action_cameras", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<List<ProductPhotoDto>> getAllProductsByCategoryActionCameras(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        return ResponseEntity.ok().body(productService.getAllProductsByCategoryActionCameras(pageable));
    }
    @GetMapping(value = "/accessories", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<List<ProductPhotoDto>> getAllProductsByCategoryAccessories(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        return ResponseEntity.ok().body(productService.getAllProductsByCategoryAccessories(pageable));
    }
    @GetMapping(value = "/racer_drones", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<List<ProductPhotoDto>> getAllProductsByCategoryRacerDrones(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        return ResponseEntity.ok().body(productService.getAllProductsByCategoryRacerDrones(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> getAllProductsByTitle(String title, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(productService.getAllProductsByTitle(title, pageable));
    }

    @PostMapping("/create_product")
    public ResponseEntity<Long> createProduct(@RequestBody ProductDto productDto) {
        return ResponseEntity.ok().body(productService.createProduct(productDto));
    }

    @PutMapping(value = "/product/{productId}", produces = MediaType.APPLICATION_JSON_VALUE)
    public ResponseEntity<Void> addPictureInProduct(@PathVariable Long productId, @ModelAttribute MultipartFile file) throws IOException {
        productService.addPictureInProduct(productId, file);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/add_product/{productId}")
    public ResponseEntity<Void> addProductInOrder(@PathVariable Long id, @PathVariable Long productId){
        productService.addProductInOrder(id, productId);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/delete_product/{productId}")
    public ResponseEntity<Void> deleteProductFromOrder(@PathVariable Long id, @PathVariable Long productId){
        productService.deleteProductFromOrder(id, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping(value = "/products", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.IMAGE_JPEG_VALUE})
    public ResponseEntity<List<ProductPhotoDto>> getAllProducts(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) throws IOException {
        return ResponseEntity.ok().body(productService.getAllProducts(pageable));
    }
}
