package io.lana.libman.core.tag.repo;

import io.lana.libman.core.tag.TaggedEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.NoRepositoryBean;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

@NoRepositoryBean
public interface TaggedRepo<T extends TaggedEntity> extends PagingAndSortingRepository<T, String> {
    Page<T> findAllByNameLike(String name, Pageable pageable);

    @Override
    List<T> findAll();
}
