package com.spring.marketplace.http.controller;

import com.spring.marketplace.handler.dto.ChatCreateDto;
import com.spring.marketplace.handler.dto.UserChatDto;
import com.spring.marketplace.handler.service.ChatService;
import com.spring.marketplace.handler.service.MessageService;
import com.spring.marketplace.handler.service.UserChatService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;

@Controller
@RequestMapping("/chats")
@RequiredArgsConstructor
public class ChatController {

    private final ChatService chatService;

    @GetMapping()
    public String getAll(Model model, Authentication authentication) {
        model.addAttribute("chats", chatService.getAllChatsByUsername(authentication.getName()));
        return "chats/chats";
    }

    @GetMapping("/{chatId}")
    public String getById(Model model, @PathVariable("chatId") BigInteger chatId) {
        model.addAttribute("chat", chatService.getChatById(chatId));
        return "chats/chat";
    }

    @PostMapping()
    public String create(Authentication authentication, ChatCreateDto chat) {
        chat.setBuyer(authentication.getName());
        return "redirect:/chats/" + chatService.saveChat(chat).getId();
    }

    @PostMapping("/{chatId}/delete")
    public String delete(Authentication authentication, @PathVariable("chatId") BigInteger chatId) {
        chatService.deleteChat(authentication.getName(), chatId);
        return "redirect:/chats";
    }

}
