package com.spring.marketplace.http.controller;

import com.spring.marketplace.database.model.Role;
import com.spring.marketplace.handler.dto.*;
import com.spring.marketplace.handler.service.ProductService;
import com.spring.marketplace.handler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.ObjectError;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;

@Controller
@RequestMapping("/users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final ProductService productService;

    @ModelAttribute
    private List<Role> roles() {
        return Arrays.asList(Role.values());
    }

    @GetMapping()
    public String getAll(Model model, UserFilter filter, Pageable pageable) {
        Page<UserReadDto> users = userService.getAllUsers(filter, pageable);
        model.addAttribute("users", PageResponse.of(users));
        model.addAttribute("filter", filter);
        model.addAttribute("roles", roles());
        return "user/users";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") BigInteger id, Model model, Pageable pageable) {
        Page<ProductDto> products = productService.getAllProducts(
                ProductFilter.builder()
                        .owner(id)
                        .build(),
                pageable);
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("page", pageable);
        model.addAttribute("products", PageResponse.of(products));
        return "user/user";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") BigInteger id, Model model) {
        model.addAttribute("user", userService.getUserById(id));
        model.addAttribute("roles", roles());
        return "user/update";
    }

    @PostMapping()
    public String registration(@ModelAttribute("repeatedPassword") String repeatedPassword,
                        @ModelAttribute("user") @Validated UserCreateDto user,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (!user.getPassword().equals(repeatedPassword)) {
            bindingResult.addError(new ObjectError("password", "Passwords do not match"));
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("user", user);
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/registration";
        }
        userService.saveUser(user);
        return "redirect:/login";
    }

    @PostMapping("/{id}/update")
    public String update(@ModelAttribute("repeatedPassword") String repeatedPassword,
                         @ModelAttribute("user") @Validated UserUpdateDto user,
                         BindingResult bindingResult,
                         RedirectAttributes redirectAttributes) {
        if (!user.getPassword().isBlank() && !user.getPassword().equals(repeatedPassword)) {
            bindingResult.addError(new ObjectError("password", "Passwords do not match"));
        }
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("product", userService.getUserById(user.getId()));
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/users/" + user.getId() + "/update";
        }
        return "redirect:/users/" + userService.updateUser(user).getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") BigInteger id) {
        userService.deleteUser(id);
        return "redirect:/users";
    }
}
