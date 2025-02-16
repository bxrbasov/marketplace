package com.spring.marketplace.handler.service;

import com.querydsl.core.types.Predicate;
import com.spring.marketplace.database.model.Role;
import com.spring.marketplace.database.querydsl.QPredicates;
import com.spring.marketplace.database.repository.UserRepository;
import com.spring.marketplace.handler.dto.UserCreateDto;
import com.spring.marketplace.handler.dto.UserFilter;
import com.spring.marketplace.handler.dto.UserReadDto;
import com.spring.marketplace.handler.dto.UserUpdateDto;
import com.spring.marketplace.utils.exception.ApplicationException;
import com.spring.marketplace.utils.exception.ErrorType;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.convert.ConversionService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.math.BigInteger;
import java.util.*;

import static com.spring.marketplace.database.model.QUser.user;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService implements UserDetailsService {

    private final UserRepository userRepository;
    private final ConversionService conversionService;
    private final PasswordEncoder passwordEncoder;


    public Page<UserReadDto> getAllUsers(UserFilter filter, Pageable pageable) {
        Predicate predicate = QPredicates.builder()
                .add(filter.getId(), user.id::eq)
                .add(filter.getUsername(), user.username::containsIgnoreCase)
                .add(filter.getEmail(), user.email::containsIgnoreCase)
                .add(filter.getRole(), user.role::eq)
                .add(filter.getCreatedAtFrom(), user.createdAt::goe)
                .add(filter.getCreatedAtTo(), user.createdAt::loe)
                .build();
        return Optional.of(userRepository.findAll(predicate, pageable))
                .filter((item)->(!item.isEmpty()))
                .map(page -> {
                    log.info("Find all the users from page {}", page.getNumber());
                    return page.map(user -> conversionService.convert(user, UserReadDto.class));
                })
                .orElseThrow(() -> {
                    log.error("No users found");
                    return new ApplicationException(ErrorType.NO_USERS_FOUND);
                });
    }

    public List<UserReadDto> getAllUsers(int pageNo, int pageSize) {
        return Optional.of(userRepository.findAll(PageRequest.of(pageNo, pageSize)))
                .filter((item)->(!item.isEmpty()))
                .map(page -> {
                    log.info("Find all the users from page {}", page.getNumber());
                    return page.map(user -> conversionService.convert(user, UserReadDto.class)).stream().toList();
                })
                .orElseThrow(() -> {
                    log.error("No users found");
                    return new ApplicationException(ErrorType.NO_USERS_FOUND);
                });
    }

    public UserReadDto getUserById(BigInteger id) {
        UserReadDto user = userRepository.findById(id)
                .map(item -> conversionService.convert(item, UserReadDto.class))
                .orElseThrow(() -> {
                    log.error("User with id {} not found", id);
                    return new ApplicationException(ErrorType.USER_NOT_FOUND);
                });
        log.info("User found: {}", user);

        return user;
    }

    @Transactional
    public UserCreateDto saveUser(UserCreateDto user) {
        userRepository.findByUsername(user.getUsername()).ifPresentOrElse((item) ->{
            log.error("User with username {} already exists", item.getUsername());
            throw new ApplicationException(ErrorType.UNIQUE_CONSTRAINT_EXCEPTION_USERNAME);
        },() ->{
            user.setRole(Role.USER);
            userRepository.save(Objects.requireNonNull(conversionService.convert(user, com.spring.marketplace.database.model.User.class)));
            log.info("User saved: {}", user);
        });

        return user;
    }

    @Transactional
    public void deleteUser(BigInteger id) {
        userRepository.findById(id)
                .ifPresentOrElse(item -> userRepository.deleteById(item.getId()),
                        () -> {
                            log.error("User with id {} was not deleted", id);
                            throw new ApplicationException(ErrorType.USER_NOT_FOUND);});
        log.info("User with id {} deleted", id);
    }

    @Transactional
    public UserReadDto updateUser(UserUpdateDto user) {
        userRepository.findById(user.getId())
                .ifPresentOrElse(item -> {
                    userRepository.findByUsername(user.getUsername())
                            .ifPresentOrElse((element)->{
                                log.error("User with this username {} already exists", element.getUsername());
                                if(element.getId().equals(user.getId())) {
                                    if(user.getPassword().isBlank()) {
                                        user.setPassword(item.getPassword());
                                    } else {
                                        user.setPassword(passwordEncoder.encode(user.getPassword()));
                                    }
                                    userRepository.save(Objects.requireNonNull(conversionService.convert(user, com.spring.marketplace.database.model.User.class)));
                                } else {
                                    throw new ApplicationException(ErrorType.UNIQUE_CONSTRAINT_EXCEPTION_USERNAME);
                                }
                            },() ->{
                                if(user.getPassword().isBlank()) {
                                    user.setPassword(item.getPassword());
                                } else {
                                    user.setPassword(passwordEncoder.encode(user.getPassword()));
                                }
                                userRepository.save(Objects.requireNonNull(conversionService.convert(user, com.spring.marketplace.database.model.User.class)));
                                log.info("User updated: {}", user);
                            });
                }, () -> {log.error("User dont exists");
                    throw new ApplicationException(ErrorType.USER_DONT_EXISTS);
                });

        return conversionService.convert(user, UserReadDto.class);
    }

    public UserReadDto getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .map(user -> conversionService.convert(user, UserReadDto.class))
                .orElseThrow();
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        System.out.println(userRepository.findByUsername(username));
        return userRepository.findByUsername(username)
                .map(user -> new User(
                        user.getUsername(),
                        user.getPassword(),
                        Collections.singleton(user.getRole())
                        )
                )
                .orElseThrow(() -> new UsernameNotFoundException("Failed to retrieve user: " + username));
    }
}
