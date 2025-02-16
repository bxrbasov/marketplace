package com.spring.marketplace.database.repository;

import com.spring.marketplace.database.model.Product;
import com.spring.marketplace.database.model.User;
import jakarta.persistence.NamedEntityGraph;
import org.springframework.data.jpa.repository.*;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface ProductRepository extends
        JpaRepository<Product, UUID>,
        QuerydslPredicateExecutor<Product>,
        RevisionRepository<Product, UUID, Integer>,
        JpaSpecificationExecutor<Product> {

    Optional<Product> findById(UUID id);

    List<Product> findByOwner(User user);

    Optional<Product> findBySku(String sku);

    @Modifying
    @Query("update Product p set p.price = p.price * 1.10")
    void updateAllProductsPrice();

    @EntityGraph(attributePaths = {"favorites"})
    @Query("select p from Product p where p.id=:id")
    Product findByIdWithFavorites(@Param("id") UUID id);
}
