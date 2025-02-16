package com.spring.marketplace.http.controller;

import com.spring.marketplace.handler.dto.MessageDto;
import com.spring.marketplace.handler.service.MessageService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.math.BigInteger;
import java.time.Instant;

@Controller
@RequestMapping()
@RequiredArgsConstructor
public class MessageController {

    private final MessageService messageService;

    @PostMapping("/chats/{chatId}/messages")
    public String create(Authentication authentication,
                         @PathVariable("chatId") BigInteger chatId,
                         @ModelAttribute("content") String content) {
        messageService.saveMessage(
                MessageDto.builder()
                        .chatId(chatId)
                        .owner(authentication.getName())
                        .content(content)
                        .createdAt(Instant.now())
                        .build()
        );
        return "redirect:/chats/" + chatId;
    }
}
