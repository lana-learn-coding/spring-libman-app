package io.lana.libman.core.tag;

import io.lana.libman.core.book.BookInfo;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;
import org.hibernate.annotations.SortNatural;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Author extends TaggedEntity {
    @OneToMany(mappedBy = "author")
    private Set<BookInfo> books = new LinkedHashSet<>();

    private String image;

    @Formula("(SELECT COUNT(b.id) FROM book_info b WHERE b.author_id = id)")
    private int booksCount;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @Column(name = "date_of_death")
    private LocalDate dateOfDeath;
}
