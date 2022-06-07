package io.lana.libman.support.security;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

@Component
class DefaultAuthFacadeImpl implements AuthFacade<UserDetails> {
}
