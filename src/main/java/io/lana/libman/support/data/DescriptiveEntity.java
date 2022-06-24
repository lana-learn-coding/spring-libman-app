package io.lana.libman.support.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;

@Getter
@Setter
@MappedSuperclass
public abstract class DescriptiveEntity extends NamedEntity implements Descriptive {
    @Column(columnDefinition = "TEXT")
    protected String description;
}
