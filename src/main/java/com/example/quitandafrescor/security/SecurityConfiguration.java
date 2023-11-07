package com.example.quitandafrescor.security;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

@Configuration
@EnableWebSecurity
public class SecurityConfiguration {

        private final SecurityFilter securityFilter;

        public SecurityConfiguration(SecurityFilter securityFilter) {
                this.securityFilter = securityFilter;
        }

        @Bean
        public SecurityFilterChain securityFilterChain(HttpSecurity httpSecurity) throws Exception {
                AntPathRequestMatcher antPathRequestMatcher1 = new AntPathRequestMatcher("/user/login",
                                HttpMethod.POST.toString());
                AntPathRequestMatcher antPathRequestMatcher2 = new AntPathRequestMatcher("/user/register",
                                HttpMethod.POST.toString());
                AntPathRequestMatcher antPathRequestMatcher3 = new AntPathRequestMatcher("/product",
                                HttpMethod.POST.toString());
                AntPathRequestMatcher antPathRequestMatcher4 = new AntPathRequestMatcher("/product",
                                HttpMethod.PUT.toString());
                AntPathRequestMatcher antPathRequestMatcher5 = new AntPathRequestMatcher("/product",
                                HttpMethod.DELETE.toString());
                AntPathRequestMatcher antPathRequestMatcher6 = new AntPathRequestMatcher("/product",
                                HttpMethod.GET.toString());
                AntPathRequestMatcher antPathRequestMatcher7 = new AntPathRequestMatcher("/user",
                                HttpMethod.GET.toString());
                AntPathRequestMatcher antPathRequestMatcher8 = new AntPathRequestMatcher("/user",
                                HttpMethod.PUT.toString());
                AntPathRequestMatcher antPathRequestMatcher9 = new AntPathRequestMatcher("/user",
                                HttpMethod.DELETE.toString());

                return httpSecurity
                                .csrf(csrf -> csrf.disable())
                                .sessionManagement(session -> session
                                                .sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                                .authorizeHttpRequests(authorize -> authorize
                                                .requestMatchers(antPathRequestMatcher1).permitAll()
                                                .requestMatchers(antPathRequestMatcher2).permitAll()
                                                .requestMatchers(antPathRequestMatcher3).hasRole("ADMIN")
                                                .requestMatchers(antPathRequestMatcher4).hasRole("ADMIN")
                                                .requestMatchers(antPathRequestMatcher5).hasRole("ADMIN")
                                                .requestMatchers(antPathRequestMatcher6).permitAll()
                                                .requestMatchers(antPathRequestMatcher7).permitAll()
                                                .requestMatchers(antPathRequestMatcher8).hasRole("CLIENT")
                                                .requestMatchers(antPathRequestMatcher9).hasRole("CLIENT")

                                                .anyRequest().authenticated())
                                .addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class)
                                .build();
        }

        @Bean
        public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration)
                        throws Exception {
                return authenticationConfiguration.getAuthenticationManager();
        }

        @Bean
        public PasswordEncoder passwordEncoder() {
                return new BCryptPasswordEncoder();
        }

}
