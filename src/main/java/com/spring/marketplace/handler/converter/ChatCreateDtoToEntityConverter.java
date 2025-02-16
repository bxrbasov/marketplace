package com.spring.marketplace.handler.converter;

import com.spring.marketplace.database.model.Chat;
import com.spring.marketplace.database.repository.ProductRepository;
import com.spring.marketplace.handler.dto.ChatCreateDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class ChatCreateDtoToEntityConverter implements Converter<ChatCreateDto, Chat> {

    private final ProductRepository productRepository;

    @Override
    public Chat convert(ChatCreateDto source) {
        return Chat.builder()
                .name(source.getName())
                .topic(productRepository.findById(source.getTopic()).orElseThrow())
                .build();
    }
}
