package com.spring.marketplace.http.rest;

import com.spring.marketplace.database.repository.ProductRepository;
import com.spring.marketplace.handler.dto.ProductDto;
import com.spring.marketplace.handler.service.ProductService;
import io.restassured.RestAssured;
import io.restassured.parsing.Parser;
import io.restassured.response.Response;
import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestConstructor;
import org.springframework.transaction.annotation.Transactional;
import java.time.Instant;
import java.util.HashMap;
import java.util.UUID;

import static io.restassured.RestAssured.*;
import static io.restassured.matcher.RestAssuredMatchers.*;
import static org.hamcrest.Matchers.*;

@TestConstructor(autowireMode = TestConstructor.AutowireMode.ALL)
@Transactional(readOnly = true)
@RequiredArgsConstructor
@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ProductRestControllerTest {

    private final ProductService productService;
    private final ProductRepository productRepository;

    @BeforeAll
    static void setUp() {
        RestAssured.baseURI = "http://localhost:8080/api/v1";
        RestAssured.basePath = "/products";
        RestAssured.port = 8080;
        RestAssured.defaultParser = Parser.JSON;
    }

    @Test
    @Order(4)
    void getAllProducts() {
        given()
                .contentType("application/json")
                .param("pageNo", "0")
                .param("pageSize", "5")
        .when()
                .get()
        .then()
                .statusCode(200)
                .and()
                .body("$", hasSize(5));
    }

    @Test
    @Order(3)
    void getProductById() {
        ProductDto product = productService.getProductById(UUID.fromString(productRepository.findBySku("New Product SKU").get().getId().toString()));

        given()
                .contentType("application/json")
        .when()
                .get("/" + productRepository.findBySku("New Product SKU").get().getId().toString())
        .then()
                .statusCode(200)
                .and()
                .body("name", equalTo(product.getName()))
                .and()
                .body("category", equalTo(product.getCategory().toString()))
                .and()
                .body("sku", equalTo(product.getSku()))
                .and()
                .body("description", equalTo(product.getDescription()));
    }

    @Test
    @Order(5)
    void deleteProductById() {
        Response response = given()
                .contentType("application/json")
        .when()
                .delete("/" + productRepository.findBySku("New Product SKU").get().getId().toString())
        .then()
                .statusCode(200)
                .extract().response();
        System.out.println(response.getBody().asString());
    }

    @Test
    @Order(1)
    void createProduct() {
        HashMap<String, String> map = new HashMap<>();
        map.put("name", "Product Name");
        map.put("description", "Product Description");
        map.put("category", "CAR");
        map.put("sku", "Product SKU");
        map.put("price", "1000000");
        map.put("quantity", "5");

        given()
                .contentType("application/json")
                .body(map)
        .when()
                .post()
        .then()
                .statusCode(200);
    }

    @Test
    @Order(2)
    void updateProduct() {
        HashMap<String, String> map = new HashMap<>();
        map.put("id", productRepository.findBySku("Product SKU").get().getId().toString());
        map.put("name", "New Product Name");
        map.put("sku", "New Product SKU");
        map.put("description", "Product Description");
        map.put("category", "CAR");
        map.put("price", "1000000");
        map.put("quantity", "5");
        map.put("createdBy", "admin");
        map.put("createdAt", Instant.now().toString());

        given()
                .contentType("application/json")
                .body(map)
        .when()
                .put()
        .then()
                .statusCode(200);
    }
}