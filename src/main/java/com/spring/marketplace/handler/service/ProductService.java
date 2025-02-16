package com.spring.marketplace.handler.service;

import com.querydsl.core.types.Predicate;
import com.spring.marketplace.database.model.Category;
import com.spring.marketplace.database.model.Product;
import com.spring.marketplace.database.querydsl.QPredicates;
import com.spring.marketplace.database.repository.ProductRepository;
import com.spring.marketplace.handler.dto.ProductDto;
import com.spring.marketplace.handler.dto.ProductFilter;
import com.spring.marketplace.handler.specification.ProductSpecification;
import com.spring.marketplace.utils.exception.ApplicationException;
import com.spring.marketplace.utils.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.time.Instant;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.UUID;

import static com.spring.marketplace.database.model.QProduct.*;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ProductService {

    private final ProductRepository productRepository;
    private final ConversionService conversionService;
    private final ReportService reportService;

    public Page<ProductDto> getAllProducts(ProductFilter filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(filter.getName(), product.name::containsIgnoreCase)
                .add(filter.getDescription(), product.description::containsIgnoreCase)
                .add(filter.getCategory(), product.category::eq)
                .add(filter.getPriceFrom(), product.price::goe)
                .add(filter.getPriceTo(), product.price::loe)
                .add(filter.getQuantityFrom(), product.quantity::goe)
                .add(filter.getQuantityTo(), product.quantity::loe)
                .add(filter.getSku(), product.sku::eq)
                .add(filter.getCreatedAtFrom(), product.createdAt::after)
                .add(filter.getCreatedAtTo(), product.createdAt::before)
                .add(filter.getOwner(), product.owner.id::eq)
                .build();
        return Optional.of(productRepository.findAll(predicate, pageable))
                .filter((item)->(!item.isEmpty()))
                .map(page -> {
                    log.info("Find all the products from page {}", page.getNumber());
                    return page.map(product -> conversionService.convert(product, ProductDto.class));
                })
                .orElseThrow(() -> {
                    log.error("No products found");
                    return new ApplicationException(ErrorType.NO_PRODUCTS_FOUND);
                });
    }

    public ProductDto getProductById(UUID id) {
        ProductDto product = productRepository.findById(id)
                .map(item -> conversionService.convert(item, ProductDto.class))
                .orElseThrow(() -> {
                    log.error("Product with id {} not found", id);
                    return new ApplicationException(ErrorType.PRODUCT_NOT_FOUND);
                });
        log.info("Product found: {}", product);

        return product;
    }

    @Transactional
    public ProductDto saveProduct(ProductDto product) {
        productRepository.findBySku(product.getSku()).ifPresentOrElse((item) ->{
            log.error("Product with sku {} already exists", item.getSku());
            throw new ApplicationException(ErrorType.UNIQUE_CONSTRAINT_EXCEPTION_SKU);
        },() ->{
            productRepository.save(Objects.requireNonNull(conversionService.convert(product, Product.class)));
            log.info("Product saved: {}", product);
        });

        return product;
    }

    @Transactional
    public void deleteProduct(UUID id) {
        productRepository.findById(id)
                .ifPresentOrElse(item -> productRepository.deleteById(item.getId()),
                        () -> {
                            log.error("Product with id {} was not deleted", id);
                            throw new ApplicationException(ErrorType.PRODUCT_NOT_FOUND);});
        log.info("Product with id {} deleted", id);
    }

    @Transactional
    public ProductDto updateProduct(ProductDto product) {
        productRepository.findById(product.getId())
                .ifPresentOrElse(item -> {
                    productRepository.findBySku(product.getSku())
                            .ifPresentOrElse((element)->{
                                log.error("Product with this sku {} already exists", element.getSku());
                                if(element.getId().equals(product.getId())) {
                                    productRepository.save(Objects.requireNonNull(conversionService.convert(product, Product.class)));
                                } else {
                                    throw new ApplicationException(ErrorType.UNIQUE_CONSTRAINT_EXCEPTION_SKU);
                                }
                            },() ->{
                                productRepository.save(Objects.requireNonNull(conversionService.convert(product, Product.class)));
                                log.info("Product updated: {}", product);
                            });
                }, () -> {log.error("Product dont exists");
                    throw new ApplicationException(ErrorType.PRODUCT_DONT_EXISTS);
                });

        return product;
    }

    @Transactional
    public List<ProductDto> searchProductsWithFilter(ProductFilter productFilter) {
        List<Product> productsList = Optional.of(productRepository.findAll(
                Specification.where(ProductSpecification.byName(productFilter.getName()))
                        .and(ProductSpecification.byQuantity(productFilter.getQuantityFrom()))
                        .and(ProductSpecification.byPrice(productFilter.getPriceTo()))
                        .and(ProductSpecification.byCatygory(productFilter.getCategory()))
        )).orElseThrow(() -> {
            log.error("No products found");
            return new ApplicationException(ErrorType.NO_PRODUCTS_FOUND);
        });

        reportService.exportAllProductsInXlsxFile(productsList);
        log.info("Found {} products", productsList.size());
        return productsList.stream().map((product) -> conversionService.convert(product, ProductDto.class)).toList();
    }

}