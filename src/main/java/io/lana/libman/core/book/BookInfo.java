package io.lana.libman.core.book;

import io.lana.libman.core.tag.Author;
import io.lana.libman.core.tag.Genre;
import io.lana.libman.core.tag.Publisher;
import io.lana.libman.core.tag.Series;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.data.Named;
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
public class BookInfo extends AuditableEntity implements Named {
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
    @JoinColumn(foreignKey = @ForeignKey(name = "author_id", foreignKeyDefinition = "ON DELETE SET NULL"))
    private Author author;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "publisher_id", foreignKeyDefinition = "ON DELETE SET NULL"))
    private Publisher publisher;


    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "series_id", foreignKeyDefinition = "ON DELETE SET NULL"))
    private Series series;

    @ManyToMany
    @JoinTable(
            name = "book_info_genres",
            joinColumns = {@JoinColumn(name = "book_info_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")},
            foreignKey = @ForeignKey(name = "book_info_id", foreignKeyDefinition = "ON DELETE SET NULL"),
            inverseForeignKey = @ForeignKey(name = "genre_id", foreignKeyDefinition = "ON DELETE SET NULL")
    )
    private Set<Genre> genres = new HashSet<>();


    @Transient
    @Override
    public String getName() {
        return title;
    }

    @Override
    public void setName(String name) {
        this.title = name;
    }
}
