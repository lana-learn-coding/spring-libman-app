package io.lana.libman.core.book;

import io.lana.libman.core.tag.Author;
import io.lana.libman.core.tag.Genre;
import io.lana.libman.core.tag.Publisher;
import io.lana.libman.core.tag.Series;
import io.lana.libman.support.data.AuditableEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class BookInfo extends AuditableEntity {
    @NotBlank
    @Column(nullable = false)
    private String title;

    private String image;

    @Column(columnDefinition = "TEXT")
    private String about;

    @Column(name = "`year`")
    private Integer year;

    @Formula("(SELECT COUNT(b.id) FROM book b WHERE b.book_info_id = id)")
    private int booksCount;

    @OneToMany(mappedBy = "info")
    private Set<Book> books = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(name = "author_id")
    private Author author;

    @ManyToOne
    @JoinColumn(name = "publisher_id")
    private Publisher publisher;


    @ManyToOne
    @JoinColumn(name = "series_id")
    private Series series;

    @ManyToMany
    @JoinTable(
            name = "book_info_genres",
            joinColumns = {@JoinColumn(name = "book_info_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")}
    )
    private Set<Genre> genres = new HashSet<>();
}
