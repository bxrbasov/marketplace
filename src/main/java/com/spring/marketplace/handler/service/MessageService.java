package com.spring.marketplace.handler.service;

import com.spring.marketplace.database.model.Chat;
import com.spring.marketplace.database.model.Message;
import com.spring.marketplace.database.repository.ChatRepository;
import com.spring.marketplace.database.repository.MessageRepository;
import com.spring.marketplace.handler.dto.MessageDto;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MessageService {

    private final MessageRepository messageRepository;
    private final ConversionService conversionService;
    private final ChatRepository chatRepository;

    public List<MessageDto> getAllMessageByChat(BigInteger chatId) {
        Chat chat = chatRepository.findById(chatId).orElseThrow();
        Sort sort = Sort.by(Sort.Direction.ASC, "createdAt");
        return messageRepository.findAllByChat(chat, sort).stream()
                .map(message -> conversionService.convert(message, MessageDto.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public void saveMessage(MessageDto message) {
        messageRepository.save(Objects.requireNonNull(conversionService.convert(message, Message.class)));
        log.info("Chat message: {}", message);
    }

}
