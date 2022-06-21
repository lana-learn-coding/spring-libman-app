package io.lana.libman.core.book;

import io.lana.libman.core.book.support.BookDetails;
import io.lana.libman.core.tag.Author;
import io.lana.libman.core.tag.Genre;
import io.lana.libman.core.tag.Publisher;
import io.lana.libman.core.tag.Series;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.data.Named;
import io.lana.libman.support.data.NamedEntity;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.Formula;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Getter
@Setter
@Entity
public class BookInfo extends AuditableEntity implements Named, BookDetails {
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

    @Formula("(SELECT COUNT(b.id) FROM book b WHERE b.book_info_id = id AND b.status = 'AVAILABLE')")
    private int availableBooksCount;

    @OneToMany(mappedBy = "info")
    private Set<Book> books = new LinkedHashSet<>();

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "author_id", foreignKeyDefinition = "FOREIGN KEY (author_id) REFERENCES author(id) ON DELETE SET NULL"))
    private Author author;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "publisher_id", foreignKeyDefinition = "FOREIGN KEY (publisher_id) REFERENCES publisher(id) ON DELETE SET NULL"))
    private Publisher publisher;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "series_id", foreignKeyDefinition = "FOREIGN KEY (series_id) REFERENCES series(id) ON DELETE SET NULL"))
    private Series series;

    @ManyToMany
    @JoinTable(
            name = "book_info_genres",
            joinColumns = {@JoinColumn(name = "book_info_id")},
            inverseJoinColumns = {@JoinColumn(name = "genre_id")},
            foreignKey = @ForeignKey(name = "book_info_id", foreignKeyDefinition = "FOREIGN KEY (book_info_id) REFERENCES book_info(id) ON DELETE CASCADE"),
            inverseForeignKey = @ForeignKey(name = "genre_id", foreignKeyDefinition = "FOREIGN KEY (genre_id) REFERENCES genre(id) ON DELETE CASCADE")
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

    @Override
    public String getAuthorName() {
        if (author == null) return null;
        return author.getName();
    }

    @Override
    public String getPublisherName() {
        if (publisher == null) return null;
        return publisher.getName();
    }

    @Override
    public String getSeriesName() {
        if (series == null) return null;
        return series.getName();
    }

    @Override
    public String getGenresName() {
        if (genres == null || genres.isEmpty()) return null;
        return genres.stream().map(NamedEntity::getName).collect(Collectors.joining(","));
    }

    @Override
    public Set<String> getGenresSet() {
        return genres.stream().map(NamedEntity::getName).collect(Collectors.toUnmodifiableSet());
    }
}
