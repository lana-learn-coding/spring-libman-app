package io.lana.libman.support.file;


import lombok.Getter;
import org.springframework.core.io.InputStreamSource;

import javax.validation.constraints.NotBlank;
import java.util.Set;

@Getter
public class FileViolationException extends RuntimeException {
    private final InputStreamSource source;

    private final Set<String> errors;

    public FileViolationException(InputStreamSource source, @NotBlank Set<String> errors) {
        super("Invalid file constrains");
        this.source = source;
        this.errors = errors;
    }

    public FileViolationException(InputStreamSource source, String error) {
        this(source, Set.of(error));
    }
}
