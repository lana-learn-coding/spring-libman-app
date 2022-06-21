package io.lana.libman.core.file;

import io.lana.libman.support.file.FileEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.springframework.core.io.InputStreamSource;

import javax.persistence.Entity;
import javax.persistence.Transient;
import java.util.Optional;

@Entity
@Getter
@Setter
@RequiredArgsConstructor
public class Image extends FileEntity {
    @Getter(AccessLevel.NONE)
    @Setter(AccessLevel.NONE)
    @Transient
    private final InputStreamSource source;

    protected Image() {
        this.source = null;
    }

    @Transient
    public Optional<InputStreamSource> getSource() {
        return Optional.ofNullable(source);
    }

    @Override
    @Transient
    public String getUri() {
        return "/files/images/" + getFileName();
    }

    @Transient
    public String getPath() {
        return "images/" + getFileName();
    }
}
