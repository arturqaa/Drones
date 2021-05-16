package com.artur.controllers;

import com.artur.service.ProductService;
import com.artur.service.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class ProductController {
    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping("/products")
    public ResponseEntity<Page<ProductDto>> getAllProducts(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(productService.getAllProducts(pageable));
    }

    @GetMapping("/drones")
    public ResponseEntity<Page<ProductDto>> getAllProductsByCategoryDrones(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(productService.getAllProductsByCategoryDrones(pageable));
    }

    @GetMapping("/action_cameras")
    public ResponseEntity<Page<ProductDto>> getAllProductsByCategoryActionCameras(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(productService.getAllProductsByCategoryActionCameras(pageable));
    }
    @GetMapping("/accessories")
    public ResponseEntity<Page<ProductDto>> getAllProductsByCategoryAccessories(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(productService.getAllProductsByCategoryAccessories(pageable));
    }
    @GetMapping("/racer_drones")
    public ResponseEntity<Page<ProductDto>> getAllProductsByCategoryRacerDrones(@PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(productService.getAllProductsByCategoryRacerDrones(pageable));
    }

    @GetMapping("/search")
    public ResponseEntity<Page<ProductDto>> getAllProductsByTitle(String title, @PageableDefault(sort = {"createdDate"}, direction = Sort.Direction.DESC) Pageable pageable) {
        return ResponseEntity.ok().body(productService.getAllProductsByTitle(title, pageable));
    }

    @PostMapping("/create_product")
    public ResponseEntity<ProductDto> createProduct(@RequestBody ProductDto productDto) {
        productService.createProduct(productDto);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/users/{id}/add_product/{productId}")
    public ResponseEntity<Void> addProductInOrder(@PathVariable Long id, @PathVariable Long productId){
        productService.addProductInOrder(id, productId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/users/{id}/delete_product/{productId}")
    public ResponseEntity<Void> deleteProductFromOrder(@PathVariable Long id, @PathVariable Long productId){
        productService.deleteProductFromOrder(id, productId);
        return ResponseEntity.ok().build();
    }
}
