package io.lana.libman.core.user;

import io.lana.libman.config.ConfigFacade;
import io.lana.libman.support.data.IdUtils;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.cache.CacheManager;
import org.springframework.cache.annotation.CacheConfig;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import java.util.Objects;
import java.util.Optional;

@Service
@CacheConfig(cacheNames = "user.token")
@RequiredArgsConstructor
class UserTokenServiceImpl implements UserTokenService {
    private final CacheManager cacheManager;

    private final UserRepo repo;

    private final ConfigFacade config;

    @Override
    public String createResetPasswordLink(User user) {
        return config.getAppUrl() + "/reset-password/" + createToken(user);
    }

    @Cacheable("token")
    @Override
    public String createToken(User user) {
        final var cache = Objects.requireNonNull(cacheManager.getCache("user.token"));
        final var token = IdUtils.newTimeSortableId();
        cache.put(token, user.getId());
        return token;
    }

    @CacheEvict("token")
    @Override
    public Optional<User> getUserByToken(String token) {
        final var cache = Objects.requireNonNull(cacheManager.getCache("user.token"));
        final var id = cache.get(token, String.class);
        if (StringUtils.isBlank(id)) return Optional.empty();
        return repo.findById(id);
    }
}
