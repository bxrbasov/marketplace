package com.spring.marketplace.handler.service;

import com.spring.marketplace.database.model.Chat;
import com.spring.marketplace.database.model.User;
import com.spring.marketplace.database.repository.ChatRepository;
import com.spring.marketplace.database.repository.UserRepository;
import com.spring.marketplace.handler.converter.EntityToChatReadDtoConverter;
import com.spring.marketplace.handler.dto.ChatCreateDto;
import com.spring.marketplace.handler.dto.ChatDto;
import com.spring.marketplace.handler.dto.ChatReadDto;
import com.spring.marketplace.handler.dto.UserChatDto;
import com.spring.marketplace.utils.exception.ApplicationException;
import com.spring.marketplace.utils.exception.ErrorType;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
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
public class ChatService {

    private final UserChatService userChatService;
    private final ChatRepository chatRepository;
    private final UserRepository userRepository;
    private final ConversionService conversionService;

    private final EntityToChatReadDtoConverter chatReadDtoConverter;

    public List<ChatDto> getAllChatsByUsername(String username) {
        return userChatService.getAllUserChatByUsername(username).stream()
                .map(chat -> conversionService.convert(chat, ChatDto.class))
                .collect(Collectors.toList());
    }

    public ChatReadDto getChatById(BigInteger id) {
        return chatReadDtoConverter.convert(chatRepository.findByIdWithMetadata(id)
                .orElseThrow(() -> {
                    log.error("Chat with id {} not found", id);
                    return new ApplicationException(ErrorType.PRODUCT_NOT_FOUND);
                }
                ));
    }

    @Transactional
    public ChatDto saveChat(ChatCreateDto chat) {
        Chat save = chatRepository.save(Objects.requireNonNull(conversionService.convert(chat, Chat.class)));
        User owner = userRepository.findById(chat.getOwner()).orElseThrow();
        userChatService.saveUserChat(
                UserChatDto.builder()
                        .chatId(save.getId())
                        .username(owner.getUsername())
                        .build()
        );
        if (!owner.getUsername().equals(chat.getBuyer())) {
            userChatService.saveUserChat(
                    UserChatDto.builder()
                            .chatId(save.getId())
                            .username(chat.getBuyer())
                            .build()
            );
        }
        log.info("Chat saved: {}", save);
        return conversionService.convert(save, ChatDto.class);
    }

    @Transactional
    public void deleteChat(String username, BigInteger id) {
        userChatService.deleteUserChat(
                UserChatDto.builder()
                        .username(username)
                        .chatId(id)
                        .build()
        );
//        chatRepository.findById(id)
//                .ifPresentOrElse(item -> chatRepository.deleteById(item.getId()),
//                        () -> {
//                            log.error("Chat with id {} was not deleted", id);
//                            throw new ApplicationException(ErrorType.PRODUCT_NOT_FOUND);});
//        log.info("Chat with id {} deleted", id);
    }

}
