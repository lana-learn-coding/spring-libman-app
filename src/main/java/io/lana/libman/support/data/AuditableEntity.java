package io.lana.libman.support.data;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedBy;
import org.springframework.data.annotation.LastModifiedDate;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

@Getter
@Setter
@MappedSuperclass
public abstract class AuditableEntity implements Identified {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    protected Long id;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    protected ZonedDateTime createdAt = ZonedDateTime.now();

    @Column(name = "updated_at")
    @LastModifiedDate
    protected ZonedDateTime updatedAt = ZonedDateTime.now();

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    protected String createdBy;

    @Column(name = "updated_by")
    @LastModifiedBy
    protected String updatedBy;

    @Override
    public int hashCode() {
        return 13;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        AuditableEntity other = (AuditableEntity) obj;
        return id != null && id.equals(other.getId());
    }
}
