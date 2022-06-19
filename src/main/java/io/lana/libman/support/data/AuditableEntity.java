package io.lana.libman.support.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.Id;
import javax.persistence.MappedSuperclass;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class AuditableEntity implements Identified {
    @Id
    protected String id = IdUtils.newTimeSortableId();

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    protected Instant createdAt = Instant.now();

    @Column(name = "updated_at")
    @LastModifiedDate
    protected Instant updatedAt = Instant.now();

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    protected String createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    protected String updatedBy;

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AuditableEntity other = (AuditableEntity) obj;
        return Objects.equals(getId(), other.getId());
    }
}
