package com.example.productservice.model.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import lombok.*;

import java.math.BigDecimal;

@Schema(description = "Product")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ProductDto {

    @Schema(description = "Unique ID", example = "1")
    private Long id;

    @Schema(description = "Unique product code 10 characters", example = "1234567890")
    private String code;

    @Schema(description = "Product name", example = "Test")
    private String name;

    @Schema(description = "Price is EUR cannot be less than 0", example = "100.0")
    private BigDecimal priceEur;

    @Schema(description = "Price is USD cannot be less than 0", example = "101.1")
    private BigDecimal priceUsd;

    @Schema(description = "Availability of the product", example = "true")
    private Boolean isAvailable;
}
