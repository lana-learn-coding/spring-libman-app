package io.lana.libman.support.security;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.server.ResponseStatusException;

import java.util.*;
import java.util.stream.Collectors;

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

    default void requireAllAuthorities(final Collection<GrantedAuthority> grantedAuthorities) {
        if (isNotAuthenticated()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (!hasAllAuthorities(grantedAuthorities)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    default void requireAllAuthorities(final String... authorities) {
        if (isNotAuthenticated()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (!hasAllAuthorities(authorities)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    default void requireAnyAuthorities(final Collection<GrantedAuthority> grantedAuthorities) {
        if (isNotAuthenticated()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (!hasAnyAuthorities(grantedAuthorities)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    default void requireAnyAuthorities(final String... authorities) {
        if (isNotAuthenticated()) throw new ResponseStatusException(HttpStatus.UNAUTHORIZED);
        if (!hasAnyAuthorities(authorities)) throw new ResponseStatusException(HttpStatus.FORBIDDEN);
    }

    /**
     * get authorities
     *
     * @return authorities or empty if user is not authenticated
     */
    default Collection<? extends GrantedAuthority> getAuthorities() {
        return getAuthentication().map(Authentication::getAuthorities).orElse(Collections.emptyList());
    }

    default boolean hasAllAuthorities(final Collection<GrantedAuthority> grantedAuthorities) {
        return hasAllAuthorities(grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new));
    }

    default boolean hasAllAuthorities(final String... authorities) {
        return getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .collect(Collectors.toUnmodifiableSet())
                .containsAll(List.of(authorities));
    }

    default boolean hasAnyAuthorities(final Collection<GrantedAuthority> grantedAuthorities) {
        return hasAnyAuthorities(grantedAuthorities.stream()
                .map(GrantedAuthority::getAuthority)
                .toArray(String[]::new));
    }

    default boolean hasAnyAuthorities(final String... authorities) {
        final Set<String> authoritiesSet = Set.of(authorities);
        return getAuthorities().stream().map(GrantedAuthority::getAuthority)
                .anyMatch(authoritiesSet::contains);
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
