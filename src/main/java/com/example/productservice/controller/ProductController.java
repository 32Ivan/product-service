package com.example.productservice.controller;


import com.example.productservice.model.Product;
import com.example.productservice.model.dto.CreateProductDto;
import com.example.productservice.model.dto.ProductDto;
import com.example.productservice.service.ProductService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@Tag(name = "Products", description = "Api for managing products")
@RestController
@RequestMapping("/api/v1/products")
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    private static final Logger logger = LoggerFactory.getLogger(ProductController.class);

    @Operation(summary = "Get a product by ID")
    @GetMapping("/{id}")
    public ResponseEntity<ProductDto> getProduct(@PathVariable Long id) {
        logger.info("Request to get product with ID: {}", id);
        return ResponseEntity.ok(productService.findById(id));
    }

    @Operation(summary = "Get all products")
    @GetMapping()
    public ResponseEntity<Page<ProductDto>> getAllProducts(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        logger.info("Request to get all products, page: {}, size: {}", page, size);
        Pageable pageable = PageRequest.of(page, size);
        return ResponseEntity.ok(productService.findAll(pageable));
    }

    @Operation(summary = "Create a new product")
    @PostMapping()
    public ResponseEntity<Product> createProduct(@Valid @RequestBody CreateProductDto dto) {
        logger.info("Request to create product with code: {}", dto.getCode());
        return ResponseEntity.status(HttpStatus.CREATED).body(productService.createProduct(dto));
    }

}
