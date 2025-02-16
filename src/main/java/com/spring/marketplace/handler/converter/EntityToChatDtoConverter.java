package com.spring.marketplace.handler.converter;

import com.spring.marketplace.database.model.Chat;
import com.spring.marketplace.handler.dto.ChatDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToChatDtoConverter implements Converter<Chat, ChatDto> {

    @Override
    public ChatDto convert(Chat source) {
        return ChatDto.builder()
                .id(source.getId())
                .name(source.getName())
                .build();
    }
}
