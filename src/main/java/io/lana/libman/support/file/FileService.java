package io.lana.libman.support.file;

import org.springframework.validation.BindingResult;
import org.springframework.web.multipart.MultipartFile;

import java.util.Optional;

public interface FileService<T extends FileEntity> {
    default boolean validate(MultipartFile file, BindingResult bindingResult, String field) {
        try {
            return validate(file);
        } catch (FileViolationException e) {
            for (String error : e.getErrors()) {
                bindingResult.rejectValue(field, "", error);
            }
            return false;
        }
    }

    boolean validate(MultipartFile file);

    default T save(MultipartFile file) {
        return save(createFrom(file));
    }

    T save(T file);

    Optional<T> findOne(String id);

    T createFrom(MultipartFile file);
}
