package io.lana.libman.core.tag.repo;

import io.lana.libman.core.tag.Author;
import io.lana.libman.core.tag.Genre;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepo extends PagingAndSortingRepository<Genre, String> {
}
