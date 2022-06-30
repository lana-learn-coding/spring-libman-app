package io.lana.libman.support.security;

import org.springframework.security.core.userdetails.UserDetails;

public interface AuthUser extends UserDetails {
    String getId();

    String getEmail();

    String getFirstName();

    String getLastName();

    String getAvatar();
}
