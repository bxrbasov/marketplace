package com.spring.marketplace.database.repository;

import com.spring.marketplace.database.model.Favorite;
import com.spring.marketplace.database.model.Product;
import com.spring.marketplace.database.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface FavoriteRepository extends
        JpaRepository<Favorite, BigInteger>,
        QuerydslPredicateExecutor<Favorite> {

    Optional<Favorite> findById(BigInteger id);

    Optional<Favorite> findByUserAndProduct(User user, Product product);

    @Query("select p from Favorite f" +
            " left join fetch Product p on f.product.id=p.id" +
            " where f.user.username=:username")
    List<Product> findAllProductsByUsername(@Param("username") String username);

}
