package io.lana.libman.support.security;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.AuditorAware;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
@RequiredArgsConstructor
public class DefaultAuditorAwareImpl implements AuditorAware<String> {
    private final AuthFacade<UserDetails> authFacade;

    @Override
    public Optional<String> getCurrentAuditor() {
        return authFacade.getPrincipal().map(UserDetails::getUsername);
    }
}
