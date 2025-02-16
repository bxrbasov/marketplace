package com.spring.marketplace.http.controller;

import com.spring.marketplace.database.model.Role;
import com.spring.marketplace.handler.dto.*;
import com.spring.marketplace.handler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/admin")
@RequiredArgsConstructor
public class AdminController {

    private final UserService userService;

    @ModelAttribute
    private List<Role> roles() {
        return Arrays.asList(Role.values());
    }

    @GetMapping()
    public String getAllFunctions() {
        return "admin/admin";
    }

    @GetMapping("/users")
    public String getAllUsers(Model model, UserFilter filter, Pageable pageable) {
        Page<UserReadDto> users = userService.getAllUsers(filter, pageable);
        model.addAttribute("users", PageResponse.of(users));
        model.addAttribute("filter", filter);
        model.addAttribute("roles", roles());
        return "admin/users";
    }

}
