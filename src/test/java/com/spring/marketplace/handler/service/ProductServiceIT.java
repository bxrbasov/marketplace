package com.spring.marketplace.handler.service;

import com.spring.marketplace.handler.dto.ProductDto;
import com.spring.marketplace.handler.dto.ProductFilter;
import com.spring.marketplace.integration.IntegrationTestBase;
import com.spring.marketplace.utils.exception.ApplicationException;
import lombok.RequiredArgsConstructor;
import org.assertj.core.api.AssertionsForClassTypes;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.annotation.Rollback;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.UUID;

import static com.spring.marketplace.database.model.Category.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@RequiredArgsConstructor
@Transactional
@Rollback
class ProductServiceIT extends IntegrationTestBase {

    private final ProductService productService;

    @Tag("checkdata")
    @Nested
    class CheckDefaultMethods {

        @Test
        void getAllProductsByFilter() {
            ProductDto porsche = productService.getProductById(UUID.fromString("eff67242-f2fd-43cd-a1e1-0c39285fd834"));

            PageRequest pageRequest = PageRequest.of(0, 10);
            ProductFilter productFilter = ProductFilter.builder()
                    .category(CAR)
                    .priceFrom(BigDecimal.valueOf(1_000_000))
                    .build();
            Page<ProductDto> products = productService.getAllProducts(productFilter, pageRequest);
            assertThat(products.getTotalElements()).isEqualTo(1);
            assertThat(products.get().toList().getFirst()).isEqualTo(porsche);
        }

        @Test
        void getAllProductsByFilterNotFound() {
            PageRequest pageRequest = PageRequest.of(0, 10);
            ProductFilter productFilter = ProductFilter.builder()
                    .priceFrom(BigDecimal.valueOf(100_000_000))
                    .build();
            assertThrows(ApplicationException.class, () -> productService.getAllProducts(productFilter, pageRequest));
            try {
                productService.getAllProducts(productFilter, pageRequest);
            } catch (ApplicationException e) {
                assertThat(e).hasMessageContaining("No products found");
            }
        }

        @Test
        void getAllProducts() {
            List<ProductDto> allProducts = productService.getAllProducts(new ProductFilter(), PageRequest.of(0, 10)).getContent();
            assertThat(allProducts).hasSize(6);
            assertThat(allProducts.stream().map(ProductDto::getCategory).toList()).contains(CAR, SMARTPHONE, LAPTOP, HEADPHONES, KEYBOARD, SCREEN);
        }

        @Test
        void getAllProductsNotFound() {
            assertThrows(ApplicationException.class, () -> productService.getAllProducts(new ProductFilter(), PageRequest.of(1, 10)).getContent());
            try {
                productService.getAllProducts(new ProductFilter(), PageRequest.of(1, 10)).getContent();
            } catch (ApplicationException e) {
                assertThat(e).hasMessageContaining("No products found");
            }
        }

        @Test
        void getProductById() {
            ProductDto productDto = ProductDto.builder()
                    .id(UUID.fromString("0308ff3d-5c46-445b-ba0c-05445157f21e"))
                    .build();

            ProductDto product = productService.getProductById(UUID.fromString("0308ff3d-5c46-445b-ba0c-05445157f21e"));
            assertThat(product).isEqualTo(productDto);
        }

        @Test
        void getProductByIdNotFound() {
            assertThrows(ApplicationException.class, () -> productService.getProductById(UUID.randomUUID()));
            try {
                productService.getProductById(UUID.randomUUID());
            } catch (ApplicationException e) {
                assertThat(e).hasMessageContaining("No such product");
            }
        }
    }

    @Tag("changedata")
    @Nested
    class CheckDefaultMethodsChanged {

        @BeforeEach
        void saveProduct() {
            ProductDto product = ProductDto.builder()
                    .name("Product Name")
                    .category(CAR)
                    .description("Product Description")
                    .sku("Product SKU")
                    .price(BigDecimal.valueOf(1_000_000))
                    .quantity(BigInteger.ONE)
                    .owner(BigInteger.ONE)
                    .build();
            productService.saveProduct(product);
        }

        @Test
        void checkSaveProduct() {
            List<ProductDto> products = productService.getAllProducts(new ProductFilter(), PageRequest.of(0, 10)).getContent();
            assertThat(products).hasSize(7);
            assertThat(products.stream().map(ProductDto::getCategory).filter(category -> category.equals(CAR)).count()).isEqualTo(2);
        }

        @Test
        void deleteProduct() {
            productService.deleteProduct(UUID.fromString("0308ff3d-5c46-445b-ba0c-05445157f21e"));
            assertThat(productService.getAllProducts(new ProductFilter(), PageRequest.of(0, 10)).getContent()).hasSize(6);
        }

        @Test
        void updateProduct() {
            ProductDto product = productService.getProductById(UUID.fromString("0308ff3d-5c46-445b-ba0c-05445157f21e"));
            ProductDto updatedProduct = ProductDto.builder()
                    .id(product.getId())
                    .name(product.getName())
                    .category(CAR)
                    .description(product.getDescription())
                    .sku(product.getSku())
                    .price(product.getPrice())
                    .quantity(product.getQuantity())
                    .owner(product.getOwner())
                    .createdAt(product.getCreatedAt())
                    .build();
            AssertionsForClassTypes.assertThat(productService.getAllProducts(new ProductFilter(), PageRequest.of(0, 10)).getContent()
                    .stream().map(ProductDto::getCategory).filter(category -> category.equals(CAR))
                    .count()).isEqualTo(2);
        }

        @Test
        void updateProductNotFound() {
            ProductDto updatedProduct = ProductDto.builder()
                    .id(UUID.randomUUID())
                    .build();
            assertThrows(ApplicationException.class, () -> productService.updateProduct(updatedProduct));
            try {
                productService.updateProduct(updatedProduct);
            } catch (ApplicationException e) {
                assertThat(e).hasMessageContaining("Product dont exists");
            }
        }
    }
}
