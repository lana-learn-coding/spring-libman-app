package io.lana.libman.core.tag;

import io.lana.libman.core.book.BookInfo;
import io.lana.libman.support.data.NamedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import java.util.Set;

@Getter
@Setter
@MappedSuperclass
public abstract class TaggedEntity extends NamedEntity {
    @Column(columnDefinition = "TEXT")
    protected String about;

    public abstract int getBooksCount();

    public abstract Set<BookInfo> getBooks();

    public abstract void setBooks(Set<BookInfo> books);
}
