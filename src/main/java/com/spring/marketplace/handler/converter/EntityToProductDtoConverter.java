package com.spring.marketplace.handler.converter;

import com.spring.marketplace.database.model.Product;
import com.spring.marketplace.handler.dto.ProductDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToProductDtoConverter implements Converter<Product, ProductDto> {

    @Override
    public ProductDto convert(Product source) {
        return ProductDto.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .category(source.getCategory())
                .price(source.getPrice())
                .quantity(source.getQuantity())
                .sku(source.getSku())
                .createdAt(source.getCreatedAt())
                .owner(source.getOwner().getId())
                .build();
    }
}
