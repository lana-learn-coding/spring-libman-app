package io.lana.libman.core.tag;

import io.lana.libman.core.book.BookInfo;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.validate.DateBeforeNow;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.OneToMany;
import javax.validation.constraints.NotBlank;
import java.time.LocalDate;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Author extends AuditableEntity implements Tagged {
    @NotBlank
    @Column(nullable = false)
    protected String name;

    @Column(columnDefinition = "TEXT")
    protected String about;

    @OneToMany(mappedBy = "author")
    private Set<BookInfo> books = new LinkedHashSet<>();

    private String image;

    @Formula("(SELECT COUNT(b.id) FROM book_info b WHERE b.author_id = id)")
    private int booksCount;

    @DateBeforeNow
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @DateBeforeNow
    @Column(name = "date_of_death")
    private LocalDate dateOfDeath;
}
