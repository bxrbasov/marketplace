package com.spring.marketplace.handler.converter;

import com.spring.marketplace.database.model.Chat;
import com.spring.marketplace.database.model.UserChat;
import com.spring.marketplace.handler.dto.ChatReadDto;
import com.spring.marketplace.handler.dto.ProductDto;
import com.spring.marketplace.handler.dto.UserReadDto;
import com.spring.marketplace.handler.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.ConversionService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class EntityToChatReadDtoConverter implements Converter<Chat, ChatReadDto> {

    private final ConversionService conversionService;
    private final MessageService messageService;

    @Override
    public ChatReadDto convert(Chat source) {
        return ChatReadDto.builder()
                .id(source.getId())
                .product(conversionService.convert(source.getTopic(), ProductDto.class))
                .users(source.getUsersChats().stream()
                        .map(UserChat::getUser)
                        .map(user -> conversionService.convert(user, UserReadDto.class))
                .collect(Collectors.toList()))
                .messages(messageService.getAllMessageByChat(source.getId()))
                .build();
    }
}
