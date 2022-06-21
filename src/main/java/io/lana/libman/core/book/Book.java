package io.lana.libman.core.book;

import io.lana.libman.core.tag.Shelf;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.data.Named;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@Entity
public class Book extends AuditableEntity implements Named {
    private String image;

    @Column(columnDefinition = "TEXT")
    private String note;

    @ManyToOne
    @JoinColumn(foreignKey = @ForeignKey(name = "shelf_id", foreignKeyDefinition = "FOREIGN KEY (shelf_id) REFERENCES shelf(id) ON DELETE SET NULL"))
    private Shelf shelf;

    @ManyToOne(optional = false)
    @JoinColumn(name = "book_info_id")
    private BookInfo info;

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
}
