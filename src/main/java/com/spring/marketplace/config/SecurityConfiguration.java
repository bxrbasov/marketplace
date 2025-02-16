package com.spring.marketplace.config;

import com.spring.marketplace.database.model.Role;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.util.matcher.RegexRequestMatcher;

@Configuration
public class SecurityConfiguration {

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(AbstractHttpConfigurer::disable)
                .authorizeHttpRequests(urlConfig -> urlConfig
                        .requestMatchers("/login", "/registration", "/api/v1/**", "/products/**", "/users", "/documents/**").permitAll()
                        .requestMatchers(RegexRequestMatcher.regexMatcher("/users/[0-9]+")).permitAll()
                        .requestMatchers("/favorites/**", "/chats/**").hasAnyAuthority(Role.ADMIN.getAuthority(), Role.USER.getAuthority())
                        .requestMatchers(RegexRequestMatcher.regexMatcher("/users/[0-9]+/update")).hasAnyAuthority(Role.ADMIN.getAuthority(), Role.USER.getAuthority())
                        .requestMatchers(RegexRequestMatcher.regexMatcher("/users/[0-9]+/delete")).hasAuthority(Role.ADMIN.getAuthority())
                        .requestMatchers("/admin/**").hasAuthority(Role.ADMIN.getAuthority())
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login")
                        .defaultSuccessUrl("/products")
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login")
                        .deleteCookies("JSESSIONID")
                        .permitAll()
                )
                .build();
    }
}
