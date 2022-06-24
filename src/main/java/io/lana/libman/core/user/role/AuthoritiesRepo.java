package io.lana.libman.core.user.role;

import io.lana.libman.support.data.NamedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

@NoRepositoryBean
public interface AuthoritiesRepo<T extends NamedEntity> extends PagingAndSortingRepository<T, String> {
    Page<T> findAllByNameLikeIgnoreCase(String name, Pageable pageable);
}
