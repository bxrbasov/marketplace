package com.spring.marketplace.database.repository;

import com.spring.marketplace.database.model.Chat;
import com.spring.marketplace.database.model.Message;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;

@Repository
public interface MessageRepository extends
        JpaRepository<Message, BigInteger>,
        QuerydslPredicateExecutor<Message> {

    @EntityGraph(attributePaths = {"chat", "owner"})
    List<Message> findAllByChat(Chat chat, Sort sort);

}
