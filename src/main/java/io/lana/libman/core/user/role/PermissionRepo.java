package io.lana.libman.core.user.role;

import org.springframework.data.repository.PagingAndSortingRepository;

public interface PermissionRepo extends PagingAndSortingRepository<Permission, Long> {
}
