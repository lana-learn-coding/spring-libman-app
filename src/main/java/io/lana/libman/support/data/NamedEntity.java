package io.lana.libman.support.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.MappedSuperclass;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public abstract class NamedEntity extends AuditableEntity implements Named {
    @Override
    public int hashCode() {
        return Objects.hashCode(getName());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        NamedEntity other = (NamedEntity) obj;
        return Objects.equals(getName(), other.getName());
    }
}
