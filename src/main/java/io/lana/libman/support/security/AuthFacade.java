package io.lana.libman.support.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

public interface AuthFacade<T extends UserDetails> {
    default boolean isAuthenticated() {
        return !isNotAuthenticated();
    }

    default boolean isNotAuthenticated() {
        final var authentication = SecurityContextHolder.getContext().getAuthentication();
        return authentication == null || !authentication.isAuthenticated()
                // Check for anonymous authentication, which is enabled by default.
                || authentication instanceof AnonymousAuthenticationToken;
    }

    /**
     * get Authentication object from spring security context
     *
     * @return Authentication object
     */
    default Optional<Authentication> getAuthentication() {
        return Optional.ofNullable(SecurityContextHolder.getContext().getAuthentication());
    }

    /**
     * get <b>authenticated</b> Authentication object from spring security context
     *
     * @return Authentication object
     * @throws ResponseStatusException if user is not authenticated
     */
    default Authentication requireAuthenticated() {
        if (isNotAuthenticated()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return SecurityContextHolder.getContext().getAuthentication();
    }

    /**
     * get <b>authenticated</b> authorities
     *
     * @return authorities
     * @throws ResponseStatusException if user is not authenticated
     */
    default Collection<? extends GrantedAuthority> requireAuthorities() {
        if (isNotAuthenticated()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return getAuthorities();
    }

    default Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthentication().map(Authentication::getAuthorities).orElse(Collections.emptyList());
    }

    /**
     * get <b>authenticated</b> principal
     *
     * @return authenticated principal
     * @throws ResponseStatusException if user is not authenticated
     */
    default T requirePrincipal() {
        if (isNotAuthenticated()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        return getPrincipal().orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED));
    }

    @SuppressWarnings("unchecked")
    default Optional<T> getPrincipal() {
        return getAuthentication().map(authentication -> (T) authentication.getPrincipal());
    }
}
