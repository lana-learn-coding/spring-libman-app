package io.lana.libman.core.book;

import io.lana.libman.core.book.support.BookDetails;
import io.lana.libman.core.tag.Shelf;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.data.Named;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Delegate;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import java.util.LinkedHashSet;
import java.util.Set;

@Getter
@Setter
@Entity
public class Book extends AuditableEntity implements Named, BookDetails {
    private String image;

    @Column(columnDefinition = "TEXT")
    private String note;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false)
    private Status status = Status.AVAILABLE;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "shelf_id", foreignKeyDefinition = "FOREIGN KEY (shelf_id) REFERENCES shelf(id) ON DELETE SET NULL"))
    private Shelf shelf;

    @OneToMany(mappedBy = "info")
    private Set<Book> books = new LinkedHashSet<>();

    @Delegate(types = BookDetails.class)
    @ManyToOne(optional = false)
    @JoinColumn(name = "book_info_id")
    private BookInfo info;

    public String getImage() {
        if (info == null) return image;
        return StringUtils.defaultIfBlank(image, info.getImage());
    }

    @Transient
    @Override
    public String getName() {
        return info.getName();
    }

    @Override
    public void setName(String name) {
        if (info == null) return;
        info.setName(name);
    }


    public enum Status {
        AVAILABLE,
        BORROWED,
        ARCHIVED,
    }
}
