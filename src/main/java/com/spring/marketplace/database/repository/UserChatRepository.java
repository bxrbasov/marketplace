package com.spring.marketplace.database.repository;

import com.spring.marketplace.database.model.Chat;
import com.spring.marketplace.database.model.User;
import com.spring.marketplace.database.model.UserChat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

@Repository
public interface UserChatRepository extends
        JpaRepository<UserChat, BigInteger>,
        QuerydslPredicateExecutor<UserChat> {

    @Query("select c from Chat c" +
            " left join fetch UserChat uc on c.id=uc.chat.id" +
            " where uc.user.username=:username")
    List<Chat> findAllChatsByUsername(@Param("username") String username);

    Optional<UserChat> findByUserAndChat(User user, Chat chat);

    @Query("select u from UserChat uc " +
            " left join User u on uc.user.id=u.id" +
            " where uc.chat.id=:chatId")
    List<User> findAllUserByChatId(@Param("chatId") BigInteger chatId);

}
