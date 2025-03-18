package com.example.productservice.repository;


import com.example.productservice.IntegrationTestBase;
import com.example.productservice.model.Product;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

public class ProductRepositoryTest extends IntegrationTestBase {

    @Autowired
    private ProductRepository productRepository;

    private Product product;

    @BeforeEach
    public void setUp() {
        productRepository.deleteAll();
        product = new Product(null, "4234567890", "Product A", new BigDecimal(100.0), new BigDecimal(110.0), true);
        productRepository.save(product);
    }

    @Test
    void shouldSaveProduct() {
        Product savedProduct = productRepository.save( new Product(null, "3234567890", "Product A", new BigDecimal(100.0),new BigDecimal( 110.0), true));

        assertNotNull(savedProduct.getId());
        assertEquals("3234567890", savedProduct.getCode());
        assertEquals("Product A", savedProduct.getName());
        assertEquals(new BigDecimal(100.0), savedProduct.getPriceEur());
        assertEquals(new BigDecimal(110.0), savedProduct.getPriceUsd());
    }

    @Test
    void shouldFindProductByCode() {
        Optional<Product> foundProduct = productRepository.findByCode("4234567890");

        assertTrue(foundProduct.isPresent());
        assertEquals("Product A", foundProduct.get().getName());
        assertEquals("4234567890", foundProduct.get().getCode());
    }

    @Test
    void shouldReturnEmptyWhenProductCodeNotFound() {
        Optional<Product> foundProduct = productRepository.findByCode("9999999999");

        assertFalse(foundProduct.isPresent());
    }

    @Test
    void shouldSaveMultipleProductsAndFindByCode() {
        Product product1 = new Product(null, "1334567890", "Product A", new BigDecimal(100.0), new BigDecimal(110.0), true);
        Product product2 = new Product(null, "0987654321", "Product B", new BigDecimal(150.0), new BigDecimal(160.0), false);
        productRepository.save(product1);
        productRepository.save(product2);

        Optional<Product> foundProduct = productRepository.findByCode("0987654321");

        assertTrue(foundProduct.isPresent());
        assertEquals("Product B", foundProduct.get().getName());
    }

    @Test
    void shouldReturnEmptyWhenNoProductWithCode() {
        Optional<Product> foundProduct = productRepository.findByCode("0000000000");

        assertFalse(foundProduct.isPresent());
    }
}