package com.spring.marketplace.http.controller;

import com.spring.marketplace.handler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/profile")
@RequiredArgsConstructor
public class ProfileController {

    private final UserService userService;

    @PostMapping()
    public String profile(Authentication authentication) {
        return "redirect:/users/" + userService.getUserByUsername(authentication.getName()).getId();
    }
}
