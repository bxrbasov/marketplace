package com.spring.marketplace.database.repository;

import com.spring.marketplace.database.model.Chat;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface ChatRepository extends
        JpaRepository<Chat, BigInteger>,
        QuerydslPredicateExecutor<Chat> {

    @EntityGraph(attributePaths = {"usersChats", "topic"})
    @Query("select c from Chat c where c.id=:chatId")
    Optional<Chat> findByIdWithMetadata(@Param("chatId") BigInteger id);

    @EntityGraph(attributePaths = {"usersChats"})
    @Query("select c from Chat c where c.id=:chatId")
    Chat findByIdWithUsersChats(@Param("chatId") BigInteger id);

}
