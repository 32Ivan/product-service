package com.example.productservice.mapper;

import com.example.productservice.model.Product;
import com.example.productservice.model.dto.CreateProductDto;
import com.example.productservice.model.dto.ProductDto;
import org.springframework.stereotype.Component;

@Component
public class ProductMapper {

     public Product mapDto(CreateProductDto dto) {
          return Product.builder()
                  .code(dto.getCode())
                  .name(dto.getName())
                  .priceEur(dto.getPriceEur())
                  .isAvailable(dto.getIsAvailable())
                  .build();
     }

     public ProductDto mapProductToProductDto(Product product) {
          return new ProductDto(
                  product.getId(),
                  product.getCode(),
                  product.getName(),
                  product.getPriceEur(),
                  product.getPriceUsd(),
                  product.getIsAvailable()
          );
     }
}