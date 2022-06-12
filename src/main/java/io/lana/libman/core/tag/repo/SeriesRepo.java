package io.lana.libman.core.tag.repo;

import io.lana.libman.core.tag.Genre;
import io.lana.libman.core.tag.Series;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeriesRepo extends PagingAndSortingRepository<Series, String> {
}
