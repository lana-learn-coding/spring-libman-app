package io.lana.libman.core.tag.repo;

import io.lana.libman.core.tag.Tagged;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.data.Named;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@NoRepositoryBean
public interface TaggedRepo<T extends AuditableEntity & Tagged & Named> extends PagingAndSortingRepository<T, String> {
    Page<T> findAllByNameLikeIgnoreCase(String name, Pageable pageable);

    @Override
    List<T> findAll();
}
