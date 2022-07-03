package io.lana.libman.core.user;

import java.util.Optional;

public interface UserTokenService {
    String createResetPasswordLink(User user);

    String createToken(User user);

    Optional<User> getUserByToken(String token);
}
