package com.spring.marketplace.handler.converter;

import com.spring.marketplace.database.model.Product;
import com.spring.marketplace.database.repository.UserRepository;
import com.spring.marketplace.handler.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class ProductDtoToEntityConverter implements Converter<ProductDto, Product> {

    private final UserRepository userRepository;

    @Override
    public Product convert(ProductDto source) {
        Product build = Product.builder()
                .id(source.getId())
                .name(source.getName())
                .description(source.getDescription())
                .category(source.getCategory())
                .price(source.getPrice())
                .quantity(source.getQuantity())
                .sku(source.getSku())
                .owner(userRepository.findById(source.getOwner()).get())
                .build();
        build.setCreatedAt(source.getCreatedAt());
        return build;
    }
}
