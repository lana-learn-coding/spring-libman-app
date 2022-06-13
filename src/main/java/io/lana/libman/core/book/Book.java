package io.lana.libman.core.book;

import io.lana.libman.core.tag.Shelf;
import io.lana.libman.support.data.AuditableEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Getter
@Setter
@Entity
public class Book extends AuditableEntity {
    private String image;

    @Column(columnDefinition = "TEXT")
    private String note;

    @ManyToOne
    @JoinColumn(name = "shelf_id")
    private Shelf shelf;

    @ManyToOne
    @JoinColumn(name = "book_info_id")
    private BookInfo info;
}
