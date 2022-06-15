package io.lana.libman.core.tag;

import io.lana.libman.core.book.BookInfo;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Genre extends TaggedEntity {
    @ManyToMany(mappedBy = "genres")
    private Set<BookInfo> books = new LinkedHashSet<>();

    @Formula("(SELECT COUNT(DISTINCT b.id) FROM book_info b RIGHT JOIN book_info_genres g WHERE g.genre_id = id)")
    private int booksCount;
}
