package io.lana.libman.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.provisioning.InMemoryUserDetailsManager;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
public class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers("/me/**/*", "/library/**/*").authenticated()
                .anyRequest().permitAll()

                .and()
                .formLogin()
                .loginPage("/login")
                .failureUrl("/login?error=true")

                .and()
                .rememberMe()

                .and()
                .logout();

        return http.build();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return new InMemoryUserDetailsManager(
                User.withDefaultPasswordEncoder()
                        .username("admin")
                        .password("1")
                        .roles("ADMIN")
                        .build(),
                User.withDefaultPasswordEncoder()
                        .username("user")
                        .password("1")
                        .roles("LIBRARIAN")
                        .build(),
                User.withDefaultPasswordEncoder()
                        .username("reader")
                        .password("1")
                        .roles()
                        .build()
        );
    }
}
