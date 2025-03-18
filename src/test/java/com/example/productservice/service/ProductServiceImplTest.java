package com.example.productservice.service;

import com.example.productservice.exception.ProductExistsException;
import com.example.productservice.mapper.ProductMapper;
import com.example.productservice.model.Product;
import com.example.productservice.model.dto.CreateProductDto;
import com.example.productservice.model.dto.ProductDto;
import com.example.productservice.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;
import static org.springframework.http.RequestEntity.post;
import static org.springframework.test.web.client.match.MockRestRequestMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;

@ExtendWith(MockitoExtension.class)
class ProductServiceImplTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ProductMapper productMapper;

    @Mock
    private HnbService hnbService;

    @InjectMocks
    private ProductServiceImpl productService;

    private ProductDto productDto;



    @Test
    void shouldReturnAllProducts() {
        Product product = new Product(1L, "12345", "Product A", new BigDecimal(100.00), new BigDecimal(101.1), true);
        ProductDto productDTO = new ProductDto(1L, "12345", "Product A", new BigDecimal(100.00), new BigDecimal(101.1), true);

        Page<Product> productPage = new PageImpl<>(List.of(product));
        when(productRepository.findAll(any(Pageable.class))).thenReturn(productPage);

        when(productMapper.mapProductToProductDto(any(Product.class))).thenReturn(productDTO);

        Page<ProductDto> result = productService.findAll(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0).getName()).isEqualTo("Product A");
        verify(productRepository, times(1)).findAll(any(Pageable.class));
    }

    @Test
    void shouldFindProductById() {
        Product product = new Product(1L, "12345", "Product A", new BigDecimal(100.0), new BigDecimal(110.0), true);
        ProductDto productDTO = new ProductDto(1L, "12345", "Product A", new BigDecimal(100.0), new BigDecimal(110.00), true);

        when(productRepository.findById(1L)).thenReturn(Optional.of(product));

        when(productMapper.mapProductToProductDto(any(Product.class))).thenReturn(productDTO);

        ProductDto result = productService.findById(1L);

        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(1L);
        assertThat(result.getName()).isEqualTo("Product A");
        verify(productRepository, times(1)).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        assertThrows(ProductExistsException.class, () -> productService.findById(999L));

        verify(productRepository, times(1)).findById(999L);
    }

    @Test
    void shouldCreateNewProduct() {
        CreateProductDto dto = new CreateProductDto("12345", "New Product", new BigDecimal(100.0), true);
        Product product = new Product(1L, "12345", "New Product", new BigDecimal(100.0), new BigDecimal(110.0), true);

        when(productRepository.findByCode(dto.getCode())).thenReturn(Optional.empty());
        when(hnbService.getAverageCurrencyExchangeRate()).thenReturn(new BigDecimal(1.1));
        when(productMapper.mapDto(dto)).thenReturn(product);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product result = productService.createProduct(dto);

        assertThat(result).isNotNull();
        assertThat(result.getCode()).isEqualTo("12345");
        assertThat(result.getPriceUsd().setScale(2, RoundingMode.HALF_UP)).isEqualTo(new BigDecimal("110.00").setScale(2, RoundingMode.HALF_UP));
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void shouldThrowExceptionWhenProductAlreadyExists() {
        CreateProductDto dto = new CreateProductDto("12345", "New Product", new BigDecimal(100.0), true);
        when(productRepository.findByCode(dto.getCode())).thenReturn(Optional.of(new Product()));

        assertThrows(ProductExistsException.class, () -> productService.createProduct(dto));

        verify(productRepository, never()).save(any(Product.class));
    }
}