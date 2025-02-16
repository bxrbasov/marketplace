package com.spring.marketplace.handler.service;

import com.spring.marketplace.database.model.*;
import com.spring.marketplace.database.repository.ChatRepository;
import com.spring.marketplace.database.repository.MessageRepository;
import com.spring.marketplace.database.repository.UserChatRepository;
import com.spring.marketplace.database.repository.UserRepository;
import com.spring.marketplace.handler.dto.UserChatDto;
import com.spring.marketplace.handler.dto.UserReadDto;
import com.spring.marketplace.utils.exception.ApplicationException;
import com.spring.marketplace.utils.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserChatService {

    private final UserRepository userRepository;
    private final ChatRepository chatRepository;
    private final UserChatRepository userChatRepository;

    @Transactional
    public void saveUserChat(UserChatDto userChat) {
        User user = userRepository.findByUsernameWithUsersChats(userChat.getUsername());
        Chat chat = chatRepository.findByIdWithUsersChats(userChat.getChatId());
        userChatRepository.findByUserAndChat(user, chat).ifPresentOrElse((item) ->{
            log.error("User {} in chat {} already exists", item.getUser().getUsername(), item.getChat().getName());
            throw new ApplicationException(ErrorType.UNIQUE_CONSTRAINT_EXCEPTION_SKU);
        },() ->{
            UserChat newChat = new UserChat();
            newChat.setUser(user);
            newChat.setChat(chat);
            userChatRepository.save(newChat);
            log.info("UserChat is saved: {}", newChat);
        });
    }

    @Transactional
    public void deleteUserChat(UserChatDto userChat) {
        User user = userRepository.findByUsernameWithUsersChats(userChat.getUsername());
        Chat chat = chatRepository.findByIdWithUsersChats(userChat.getChatId());
        userChatRepository.findByUserAndChat(user, chat).ifPresentOrElse((item) ->{
            log.error("User {} in chat {} already exists:", item.getUser().getUsername(), item.getChat().getName());
            user.getUsersChats().remove(item);
            chat.getUsersChats().remove(item);
            userChatRepository.delete(item);
            log.info("UserChat is deleted: {}", item);
        },() ->{
            UserChat newChat = new UserChat();
            newChat.setUser(user);
            newChat.setChat(chat);
            userChatRepository.save(newChat);
            log.info("UserChat is saved: {}", newChat);
        });
    }

    public List<Chat> getAllUserChatByUsername(String username) {
        return userChatRepository.findAllChatsByUsername(username);
    }

}
