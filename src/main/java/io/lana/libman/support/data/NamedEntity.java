package io.lana.libman.support.data;

import io.lana.libman.support.validate.Unique;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.MappedSuperclass;
import javax.validation.constraints.NotBlank;

@Getter
@Setter
@MappedSuperclass
@Unique("name")
public abstract class NamedEntity extends AuditableEntity implements Named {
    @NotBlank
    @Column(nullable = false, unique = true)
    protected String name;
}
