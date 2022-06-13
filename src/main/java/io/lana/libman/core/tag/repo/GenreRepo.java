package io.lana.libman.core.tag.repo;

import io.lana.libman.core.tag.Genre;
import org.springframework.stereotype.Repository;

@Repository
public interface GenreRepo extends TaggedRepo<Genre> {
}
