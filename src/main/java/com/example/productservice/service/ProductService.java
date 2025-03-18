package com.example.productservice.service;

import com.example.productservice.model.Product;
import com.example.productservice.model.dto.CreateProductDto;
import com.example.productservice.model.dto.ProductDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface ProductService {
    Page<ProductDto> findAll(Pageable pageable);

    ProductDto findById(Long id);

    Product createProduct(CreateProductDto dto);
}
