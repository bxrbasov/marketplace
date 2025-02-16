package com.spring.marketplace.handler.converter;

import com.spring.marketplace.database.model.Message;
import com.spring.marketplace.handler.dto.MessageDto;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class EntityToMessageDtoConverter implements Converter<Message, MessageDto> {
    @Override
    public MessageDto convert(Message source) {
        return MessageDto.builder()
                .chatId(source.getChat().getId())
                .owner(source.getOwner().getUsername())
                .content(source.getContent())
                .createdAt(source.getCreatedAt())
                .build();
    }
}
