package com.spring.marketplace.handler.dto;

import com.spring.marketplace.database.model.Category;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductFilter {
    private String name;
    private String description;
    private Category category;
    private BigDecimal priceFrom;
    private BigDecimal priceTo;
    private BigInteger quantityFrom;
    private BigInteger quantityTo;
    private String sku;
    private Instant createdAtFrom;
    private Instant createdAtTo;
    private BigInteger owner;
}
