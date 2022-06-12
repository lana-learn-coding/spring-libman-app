package io.lana.libman.core.tag.repo;

import io.lana.libman.core.tag.Author;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthorRepo extends TaggedRepo<Author> {
}
