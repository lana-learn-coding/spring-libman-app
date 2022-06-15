package io.lana.libman.core.book;

import io.lana.libman.core.tag.Shelf;
import io.lana.libman.support.data.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Book extends AuditableEntity {
    private String image;

    @Column(columnDefinition = "TEXT")
    private String note;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "shelf_id", foreignKeyDefinition = "ON DELETE SET NULL"))
    private Shelf shelf;

    @ManyToOne
    @JoinColumn(name = "book_info_id")
    private BookInfo info;
}
