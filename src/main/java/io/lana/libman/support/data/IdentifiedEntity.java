package io.lana.libman.support.data;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
public abstract class IdentifiedEntity implements Identified {
    @Id
    protected String id = IdUtils.newTimeSortableId();

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        IdentifiedEntity other = (IdentifiedEntity) obj;
        return Objects.equals(getId(), other.getId());
    }
}
