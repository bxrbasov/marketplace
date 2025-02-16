package com.spring.marketplace.database.repository;

import com.spring.marketplace.database.model.User;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.history.RevisionRepository;
import org.springframework.data.repository.query.Param;

import java.math.BigInteger;
import java.util.Optional;

public interface UserRepository extends
        JpaRepository<User, BigInteger>,
        QuerydslPredicateExecutor<User>,
        RevisionRepository<User, BigInteger, Integer> {

    Optional<User> findByUsername(String username);

    Optional<User> findById(BigInteger id);

    void deleteById(BigInteger id);

    @EntityGraph(attributePaths = {"favorites"})
    @Query("select u from User u where u.username=:username")
    User findByUsernameWithFavorites(@Param("username") String username);

    @EntityGraph(attributePaths = {"usersChats"})
    @Query("select u from User u where u.username=:username")
    User findByUsernameWithUsersChats(@Param("username") String username);
}
