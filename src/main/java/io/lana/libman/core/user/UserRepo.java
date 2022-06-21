package io.lana.libman.core.user;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;
import java.util.Optional;

public interface UserRepo extends PagingAndSortingRepository<User, Long> {
    @Query("select u from User u join fetch u.roles where u.username = :username or u.email = :username")
    Optional<User> findUserForAuth(String username);

    @Override
    List<User> findAll();
}
