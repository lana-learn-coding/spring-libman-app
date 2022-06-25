package io.lana.libman.config;

import io.lana.libman.core.user.UserRepo;
import io.lana.libman.core.user.role.Authorities;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.DelegatingPasswordEncoder;
import org.springframework.security.crypto.password.NoOpPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import java.util.Map;

@Configuration
@EnableJpaAuditing
class SecurityConfig {
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .headers()
                .frameOptions().sameOrigin()

                .and()
                .csrf()
                .ignoringAntMatchers("/h2-console/**")

                .and()
                .authorizeRequests()
                .antMatchers("/library/**/*", "/authorities/**/*")
                .hasAuthority(Authorities.LIBRARIAN)
                .antMatchers("/me/**/*")
                .authenticated()
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
    public UserDetailsService userDetailsService(UserRepo repo) {
        return username -> repo.findUserForAuth(username)
                .orElseThrow(() -> new UsernameNotFoundException("User with username " + username + " not found"));
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        final var defaultEncode = "bcrypt";
        return new DelegatingPasswordEncoder(defaultEncode, Map.of(
                defaultEncode, new BCryptPasswordEncoder(),
                "noop", NoOpPasswordEncoder.getInstance()
        ));
    }
}
