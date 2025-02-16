package com.spring.marketplace.http.controller;

import com.spring.marketplace.handler.dto.*;
import com.spring.marketplace.handler.service.FavoriteService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@Controller
@RequestMapping("/favorites")
@RequiredArgsConstructor
public class FavoriteController {

    private final FavoriteService favoriteService;

    @GetMapping()
    public String getAll(Model model, Authentication authentication, Pageable pageable) {
        Page<ProductDto> products = favoriteService.getAllFavoriteProducts(pageable, authentication.getName());
        model.addAttribute("products", PageResponse.of(products));
        return "favorite/favorites";
    }

    @PostMapping()
    public String create(Authentication authentication, @ModelAttribute("productId") UUID productId) {
        favoriteService.saveFavoriteProduct(FavoriteDto.builder()
                .username(authentication.getName())
                .productId(productId)
                .build());
        return "redirect:/products/" + productId;
    }

    @PostMapping("/delete")
    public String delete(Authentication authentication, @ModelAttribute("productId") UUID productId) {
        favoriteService.deleteFavoriteProduct(FavoriteDto.builder()
                .username(authentication.getName())
                .productId(productId)
                .build());
        return "redirect:/products/" + productId;
    }

}
