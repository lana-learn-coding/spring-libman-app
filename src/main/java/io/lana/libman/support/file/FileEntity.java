package io.lana.libman.support.file;

import io.lana.libman.support.data.IdUtils;
import io.lana.libman.support.data.Identified;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedBy;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;
import org.springframework.lang.Nullable;

import javax.persistence.*;
import javax.validation.constraints.NotBlank;
import java.time.Instant;
import java.util.Objects;

@Getter
@Setter
@MappedSuperclass
@EntityListeners(AuditingEntityListener.class)
public abstract class FileEntity implements Identified {
    @Id
    protected String id = IdUtils.newTimeSortableId();

    @NotBlank
    @Column(name = "extension", nullable = false, updatable = false)
    protected String extension = "";

    @NotBlank
    @Column(name = "mimeType", nullable = false, updatable = false)
    protected String mimeType;

    @Column(name = "created_at", nullable = false, updatable = false)
    @CreatedDate
    protected Instant createdAt = Instant.now();

    @Column(name = "created_by", updatable = false)
    @CreatedBy
    protected String createdBy;

    @Transient
    public abstract String getUri();

    public void setExtension(@Nullable String extension) {
        if (extension == null) {
            this.extension = "";
            return;
        }
        if (!extension.startsWith(".")) extension = "." + extension;
        this.extension = extension;
    }

    @Transient
    public String getFileName() {
        return id + extension;
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (obj == null) return false;
        if (getClass() != obj.getClass()) return false;
        FileEntity other = (FileEntity) obj;
        return Objects.equals(getId(), other.getId());
    }
}
