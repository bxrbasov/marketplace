package com.spring.marketplace.http.controller;

import com.spring.marketplace.handler.dto.UserCreateDto;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class RegistrationController {

    @GetMapping("/registration")
    public String registration(Model model, UserCreateDto user) {
        model.addAttribute("user", user);
        return "user/create";
    }
}
