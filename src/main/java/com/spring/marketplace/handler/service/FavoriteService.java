package com.spring.marketplace.handler.service;

import com.querydsl.core.types.Predicate;
import com.spring.marketplace.database.model.Favorite;
import com.spring.marketplace.database.model.Product;
import com.spring.marketplace.database.model.QFavorite;
import com.spring.marketplace.database.model.User;
import com.spring.marketplace.database.querydsl.QPredicates;
import com.spring.marketplace.database.repository.FavoriteRepository;
import com.spring.marketplace.database.repository.ProductRepository;
import com.spring.marketplace.database.repository.UserRepository;
import com.spring.marketplace.handler.dto.FavoriteDto;
import com.spring.marketplace.handler.dto.ProductDto;
import com.spring.marketplace.utils.exception.ApplicationException;
import com.spring.marketplace.utils.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class FavoriteService {

    private final FavoriteRepository favoriteRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;
    private final ConversionService conversionService;

    public Page<ProductDto> getAllFavoriteProducts(Pageable pageable, String username) {
        Predicate predicate = QPredicates.builder()
                .add(username, QFavorite.favorite.user.username::eq)
                .build();
        return Optional.of(favoriteRepository.findAll(predicate, pageable))
                .filter((item)->(!item.isEmpty()))
                .map(page -> {
                    log.info("Find all the products from page {}", page.getNumber());
                    return page.map(Favorite::getProduct)
                            .map(product -> conversionService.convert(product, ProductDto.class));
                }).orElse(Page.empty());
    }

    @Transactional
    public void saveFavoriteProduct(FavoriteDto favorite) {
        User user = userRepository.findByUsername(favorite.getUsername()).orElseThrow();
        Product product = productRepository.findById(favorite.getProductId()).orElseThrow();
        favoriteRepository.findByUserAndProduct(user, product).ifPresentOrElse((item) ->{
            log.error("Favorite product for user {} with SKU {} already exists", item.getUser().getUsername(), item.getProduct().getSku());
            throw new ApplicationException(ErrorType.UNIQUE_CONSTRAINT_EXCEPTION_SKU);
        },() ->{
            Favorite newFavorite = new Favorite();
            newFavorite.setUser(user);
            newFavorite.setProduct(product);
            favoriteRepository.save(newFavorite);
            log.info("Favorite product saved: {}", newFavorite);
        });
    }

    @Transactional
    public void deleteFavoriteProduct(FavoriteDto favorite) {
        User user = userRepository.findByUsernameWithFavorites(favorite.getUsername());
        Product product = productRepository.findByIdWithFavorites(favorite.getProductId());
        favoriteRepository.findByUserAndProduct(user, product).ifPresentOrElse((item) ->{
            log.error("Favorite product for user {} with sku {} already exists", item.getUser().getUsername(), item.getProduct().getSku());
            user.getFavorites().remove(item);
            product.getFavorites().remove(item);
            favoriteRepository.delete(item);
            log.info("Favorite product deleted: {}", item);
        },() ->{
            Favorite newFavorite = new Favorite();
            newFavorite.setUser(user);
            newFavorite.setProduct(product);
            favoriteRepository.save(newFavorite);
            log.info("Favorite product with id saved: {}", newFavorite);
        });
    }

    public List<ProductDto> getAllFavoriteProducts(String username) {
        return favoriteRepository.findAllProductsByUsername(username).stream()
                .map(product -> conversionService.convert(product, ProductDto.class))
                .collect(Collectors.toList());
    }

}
