package io.lana.libman.core.tag;

import io.lana.libman.core.book.BookInfo;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Publisher extends TaggedEntity {
    @OneToMany(mappedBy = "publisher")
    private Set<BookInfo> books = new LinkedHashSet<>();

    @Formula("(SELECT COUNT(b.id) FROM book_info b WHERE b.publisher_id = id)")
    private int booksCount;
}
