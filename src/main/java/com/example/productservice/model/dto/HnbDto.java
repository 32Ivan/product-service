package com.example.productservice.model.dto;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;


@Setter
@Getter
@JsonIgnoreProperties(ignoreUnknown = true)
public class HnbDto {

    @JsonProperty("srednji_tecaj")
    private String averageRate;

    public BigDecimal getAverageCurrencyToBigDecimal(){
        return new BigDecimal(averageRate.replace(",", "."));
    }

}
