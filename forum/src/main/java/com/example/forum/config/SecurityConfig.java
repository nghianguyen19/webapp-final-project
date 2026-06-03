package com.example.forum.config;

import com.example.forum.service.CustomUserDetailsService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {

    private final CustomUserDetailsService customUserDetailsService;

    public SecurityConfig(CustomUserDetailsService customUserDetailsService) {
        this.customUserDetailsService = customUserDetailsService;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())

                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/",
                                "/login",
                                "/register",
                                "/verify-otp",
                                "/resend-otp",
                                "/search",
                                "/css/**",
                                "/js/**",
                                "/images/**",
                                "/uploads/**"
                        ).permitAll()

                        .requestMatchers("/profile/me").authenticated()
                        .requestMatchers("/profile/edit").authenticated()
                        .requestMatchers("/profile/password").authenticated()

                        .requestMatchers("/admin", "/admin/**").hasRole("ADMIN")

                        .requestMatchers("/posts/new").authenticated()
                        .requestMatchers("/posts/edit/**").authenticated()
                        .requestMatchers("/posts/delete/**").authenticated()
                        .requestMatchers("/comments/**").authenticated()
                        .requestMatchers("/posts/like/**").authenticated()
                        .requestMatchers("/posts/bookmark/**").authenticated()
                        .requestMatchers("/bookmarks").authenticated()
                        .requestMatchers("/reports/**").authenticated()
                        .requestMatchers("/notifications/**").authenticated()

                        .requestMatchers("/categories/**").permitAll()
                        .requestMatchers("/profile/**").permitAll()
                        .requestMatchers("/posts/{id}").permitAll()

                        .anyRequest().authenticated()
                )

                .formLogin(form -> form
                        .loginPage("/login")
                        .loginProcessingUrl("/login")
                        .defaultSuccessUrl("/", true)
                        .failureUrl("/login?error")
                        .permitAll()
                )

                .logout(logout -> logout
                        .logoutUrl("/logout")
                        .logoutSuccessUrl("/login?logout")
                        .permitAll()
                )

                .authenticationProvider(authenticationProvider());

        return http.build();
    }

    @Bean
    public DaoAuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider provider = new DaoAuthenticationProvider(customUserDetailsService);
        provider.setPasswordEncoder(passwordEncoder());
        return provider;
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}