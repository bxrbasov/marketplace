package com.spring.marketplace.handler.converter;

import com.spring.marketplace.database.model.Message;
import com.spring.marketplace.database.repository.ChatRepository;
import com.spring.marketplace.database.repository.UserRepository;
import com.spring.marketplace.handler.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class MessageDtoToEntityConverter implements Converter<MessageDto, Message> {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;

    @Override
    public Message convert(MessageDto source) {
        return Message.builder()
                .chat(chatRepository.findById(source.getChatId()).orElse(null))
                .content(source.getContent())
                .owner(userRepository.findByUsername(source.getOwner()).orElse(null))
                .createdAt(source.getCreatedAt())
                .build();
    }
}
