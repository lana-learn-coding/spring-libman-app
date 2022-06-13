package io.lana.libman.core.tag;

import io.lana.libman.core.book.Book;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Shelf extends TaggedEntity {
    @OneToMany(mappedBy = "shelf")
    private Set<Book> books = new LinkedHashSet<>();

    @Formula("(SELECT COUNT(b.id) FROM book b WHERE b.shelf_id = id)")
    private int booksCount;

    public static Shelf storage() {
        final var shelf = new Shelf();
        shelf.id = "DEFAULT";
        shelf.name = "Default Storage";
        return shelf;
    }

    public static Shelf ofName(String name) {
        final var shelf = new Shelf();
        shelf.name = name;
        return shelf;
    }
}
