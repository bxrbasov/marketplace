package com.spring.marketplace.http.controller;

import com.spring.marketplace.database.model.Category;
import com.spring.marketplace.handler.dto.PageResponse;
import com.spring.marketplace.handler.dto.ProductDto;
import com.spring.marketplace.handler.dto.ProductFilter;
import com.spring.marketplace.handler.dto.UserReadDto;
import com.spring.marketplace.handler.service.FavoriteService;
import com.spring.marketplace.handler.service.ProductService;
import com.spring.marketplace.handler.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.Arrays;
import java.util.List;
import java.util.UUID;

@Controller
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;
    private final FavoriteService favoriteService;
    private final UserService userService;

    @ModelAttribute
    private List<Category> categories() {
        return Arrays.asList(Category.values());
    }

    @GetMapping()
    public String getAll(Model model, ProductFilter filter, Pageable pageable) {
        model.addAttribute("products", PageResponse.of(productService.getAllProducts(filter, pageable)));
        model.addAttribute("filter", filter);
        model.addAttribute("categories", categories());
        return "product/products";
    }

    @GetMapping("/{id}")
    public String getById(@PathVariable("id") UUID id, Model model, Authentication authentication) {
        ProductDto product = productService.getProductById(id);
        model.addAttribute("product", product);

        if(authentication != null) {
            List<ProductDto> products = favoriteService.getAllFavoriteProducts(authentication.getName());
            if(products.contains(product)) {
                model.addAttribute("check", true);
            } else {
                model.addAttribute("check", false);
            }
        }
        return "product/product";
    }

    @GetMapping("/create")
    public String create(Model model, ProductDto product) {
        model.addAttribute("product", product);
        model.addAttribute("categories", categories());
        model.addAttribute("user", userService.getUserByUsername(SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getName()));
        return "product/create";
    }

    @GetMapping("/{id}/update")
    public String update(@PathVariable("id") UUID id, Model model) {
        model.addAttribute("product", productService.getProductById(id));
        model.addAttribute("categories", categories());
        return "product/update";
    }

    @PostMapping()
    public String create(@ModelAttribute("product") @Validated ProductDto product,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("product", product);
            redirectAttributes.addFlashAttribute("categories", categories());
            redirectAttributes.addFlashAttribute("user", userService.getUserByUsername(SecurityContextHolder
                    .getContext()
                    .getAuthentication()
                    .getName()));
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:products/create";
        }
        productService.saveProduct(product);
        return "redirect:/products";
    }

    @PostMapping("/{id}/update")
    public String update(@PathVariable("id") UUID id,
                                @ModelAttribute("product") @Validated ProductDto product,
                                BindingResult bindingResult,
                                RedirectAttributes redirectAttributes) {
        if (bindingResult.hasErrors()) {
            redirectAttributes.addFlashAttribute("product", productService.getProductById(id));
            redirectAttributes.addFlashAttribute("categories", categories());
            redirectAttributes.addFlashAttribute("errors", bindingResult.getAllErrors());
            return "redirect:/products/" + id + "/update";
        }
        return "redirect:/products/" + productService.updateProduct(product).getId();
    }

    @PostMapping("/{id}/delete")
    public String delete(@PathVariable("id") UUID id, @ModelAttribute("product") ProductDto product) {
        productService.deleteProduct(id);
        return "redirect:/products";
    }
}
