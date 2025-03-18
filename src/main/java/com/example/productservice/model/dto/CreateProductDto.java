package com.example.productservice.model.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;

@Schema(description = "For creating a new product")
@Data
@AllArgsConstructor
@Builder
public class CreateProductDto {

    @Schema(description = "Unique product code 10 characters",example = "1234567890")
    @NotNull
    @Size(min = 10, max = 10, message = "Code must be exactly 10 characters.")
    private String code;

    @Schema(description = "Product name",example = "Test")
    @NotBlank
    private String name;

    @Schema(description = "Price is EUR cannot be less then 0",example = "100.0")
    @Min(value = 0, message = "Price in EUR cannot be less than 0.")
    private BigDecimal priceEur;

    @Schema(description = "Availability of the product",example = "true")
    @NotNull
    private Boolean isAvailable;
}
