package com.example.productservice.service;

import com.example.productservice.exception.ProductExistsException;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.model.Product;
import com.example.productservice.model.dto.CreateProductDto;
import com.example.productservice.model.dto.ProductDto;
import com.example.productservice.repository.ProductRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;


@Service
@RequiredArgsConstructor
@Transactional
public class ProductServiceImpl implements ProductService {

    private static final Logger logger = LoggerFactory.getLogger(ProductServiceImpl.class);

    private final ProductRepository productRepository;
    private final ProductMapper productMapper;
    private final HnbService hnbService;

    @Override
    @Cacheable(cacheNames = "products")
    public Page<ProductDto> findAll(Pageable pageable) {
        logger.info("Fetching all products with pageable: {}", pageable);
        Page<Product> productsPage = productRepository.findAll(pageable);
        return productsPage.map(productMapper::mapProductToProductDto);
    }

    @Override
    public ProductDto findById(Long id) {
        logger.info("Finding product with ID: {}", id);
         Product product = productRepository.findById(id)
                .orElseThrow(() ->{
                        logger.error("Product with ID {} not found", id);
                        return new ProductExistsException("Product with ID " + id + " not found.");
                });
        return productMapper.mapProductToProductDto(product);
    }

    @Override
    public Product createProduct(CreateProductDto dto) {
        logger.info("Creating product with code: {}", dto.getCode());
        if (productRepository.findByCode(dto.getCode()).isPresent()) {
            logger.error("Product with code {} already exists", dto.getCode());
            throw new ProductExistsException("Product with code " + dto.getCode() + " already exists.");
        }

        BigDecimal usdPrice = hnbService.getAverageCurrencyExchangeRate()
                .multiply(dto.getPriceEur())
                .setScale(2, BigDecimal.ROUND_HALF_UP);

        Product newProduct = productMapper.mapDto(dto);
        newProduct.setPriceUsd(usdPrice);
        Product savedProduct = productRepository.save(newProduct);
        logger.info("Product with code {} created successfully", savedProduct.getCode());
        return savedProduct;
    }
}
