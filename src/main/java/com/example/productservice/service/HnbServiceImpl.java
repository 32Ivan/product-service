package com.example.productservice.service;

import com.example.productservice.config.ApiConfig;
import com.example.productservice.exception.HnbException;
import com.example.productservice.model.dto.HnbDto;
import lombok.AllArgsConstructor;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.retry.annotation.Backoff;
import org.springframework.retry.annotation.Retryable;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;


@Service
@AllArgsConstructor
public class HnbServiceImpl implements HnbService {

    private static final Logger logger = LoggerFactory.getLogger(HnbServiceImpl.class);

    private final RestTemplate restTemplate;
    private final ApiConfig apiConfig;

    @Override
    @Cacheable(cacheNames = "currencyExchangeRate")
    public BigDecimal getAverageCurrencyExchangeRate() {
        logger.info("Fetching average currency exchange rate from HNB API.");
        try {
            List<HnbDto> response = Arrays.asList(Objects.requireNonNull(getUsdCurrency()));
            if (response.isEmpty()) {
                logger.error("No data received from HNB API.");
                throw new HnbException("No data received from HNB API.");
            }
            return response.get(0).getAverageCurrencyToBigDecimal();
        } catch (RestClientException e) {
            logger.error("Error occurred while calling HNB API: {}", e.getMessage());
            throw new HnbException("Error occurred while calling HNB API: " + e.getMessage());
        } catch (Exception e) {
            logger.error("An unexpected error occurred while calling HNB API: {}", e.getMessage());
            throw new HnbException("An unexpected error occurred while calling HNB API: " + e.getMessage());
        }
    }

    @Retryable(maxAttempts = 3, backoff = @Backoff(delay = 3000))
    private HnbDto[] getUsdCurrency() {
        logger.debug("Sending request to HNB API for USD currency rate.");
        return restTemplate.getForObject(apiConfig.getUrl(), HnbDto[].class);
    }
}




