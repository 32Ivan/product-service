package com.example.productservice.controller;

import com.example.productservice.IntegrationTestBase;
import com.example.productservice.model.Product;
import com.example.productservice.model.dto.CreateProductDto;

import com.example.productservice.model.dto.ProductDto;
import com.example.productservice.service.ProductService;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;

import org.junit.jupiter.api.Test;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;

import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;



@AutoConfigureMockMvc
class ProductControllerTest extends IntegrationTestBase {


    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductService productService;

    private ProductDto productDto;
    private CreateProductDto createProductDto;

    @BeforeEach
    public void setUp() {
        productDto = new ProductDto(1L, "1234567890", "Product A", new BigDecimal(100.0), new BigDecimal(110.0), true);
        createProductDto = new CreateProductDto("1234567890", "Product A", new BigDecimal(100.0), true);
    }

    @Test
    void shouldGetProductById() throws Exception {
        when(productService.findById(1L)).thenReturn(productDto);

        mockMvc.perform(get("/api/v1/products/{id}", 1L))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.code").value("1234567890"))
                .andExpect(jsonPath("$.name").value("Product A"));
    }

    @Test
    void shouldGetAllProducts() throws Exception {

        ProductDto productDTO = new ProductDto(1L, "12345", "Product A", new BigDecimal(100.0),new BigDecimal( 101.1), true);
        Page<ProductDto> productDTOPage = new PageImpl<>(List.of(productDTO));

        when(productService.findAll(any(Pageable.class))).thenReturn(productDTOPage);

        mockMvc.perform(get("/api/v1/products")
                        .param("page", "0")
                        .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content", hasSize(1)))
                .andExpect(jsonPath("$.content[0].id").value(1L))
                .andExpect(jsonPath("$.content[0].name").value("Product A"));
    }

    @Test
    void shouldNotCreateProductWithInvalidData() throws Exception {
        mockMvc.perform(post("/api/v1/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("{\"code\": \"\", \"name\": \"\", \"priceEur\": 0.0, \"isAvailable\": true}"))
                .andExpect(status().isBadRequest());
    }
}