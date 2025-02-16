package com.spring.marketplace.http.rest;

import com.spring.marketplace.handler.dto.ProductDto;
import com.spring.marketplace.handler.dto.ProductFilter;
import com.spring.marketplace.handler.service.ProductService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RequestBody;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("api/v1/products")
@RequiredArgsConstructor
public class ProductRestController {

    private final ProductService productService;

    @GetMapping
    public List<ProductDto> getAllProducts(@RequestParam(name = "pageNo", defaultValue = "0") int pageNo, @RequestParam(name = "pageSize", defaultValue = "10") int pageSize) {
        return productService.getAllProducts(new ProductFilter(), PageRequest.of(pageNo, pageSize)).getContent();
    }

    @GetMapping("/{id}")
    public ProductDto getProductById(@PathVariable(name = "id") UUID id) {
        return productService.getProductById(id);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteProductById(@PathVariable(name = "id") UUID id) {
        productService.deleteProduct(id);
        return ResponseEntity.ok("Product is deleted");
    }

    @PostMapping
    public ProductDto createProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.saveProduct(productDto);
    }

    @PutMapping
    public ProductDto updateProduct(@Valid @RequestBody ProductDto productDto) {
        return productService.updateProduct(productDto);
    }

    @GetMapping("/search")
    public List<ProductDto> searchProductsWithFilter(@RequestBody ProductFilter filter){
        return productService.searchProductsWithFilter(filter);
    }

}
