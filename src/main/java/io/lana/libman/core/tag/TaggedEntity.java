package io.lana.libman.core.tag;

import io.lana.libman.support.data.NamedEntity;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class TaggedEntity extends NamedEntity implements Tagged {
    @Column(columnDefinition = "TEXT")
    protected String about;

    public abstract int getBooksCount();
}
