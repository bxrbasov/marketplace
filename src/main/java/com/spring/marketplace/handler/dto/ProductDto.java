package com.spring.marketplace.handler.dto;

import com.spring.marketplace.database.model.Category;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ToString(exclude = "category")
@EqualsAndHashCode(of = "id")
public class ProductDto {

    private UUID id;

    @NotBlank(message = "Name must be not empty")
    private String name;

    @NotBlank(message = "Description must be not empty")
    @Size(min = 10, message = "Description length min 10 chars")
    private String description;

    @NotNull(message = "Category must be not empty")
    private Category category;

    @Min(value = 100, message = "Min price is 100")
    private BigDecimal price;

    @Min(value = 0, message = "Quantity must be more or equals 0")
    private BigInteger quantity;

    @NotBlank(message = "SKU must be not empty")
    private String sku;

    private Instant createdAt;

    private BigInteger owner;
}