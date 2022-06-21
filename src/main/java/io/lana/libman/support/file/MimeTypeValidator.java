package io.lana.libman.support.file;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class MimeTypeValidator implements ConstraintValidator<MimeType, MultipartFile> {
    private boolean required;

    private String[] mimes;

    @Override
    public boolean isValid(MultipartFile value, ConstraintValidatorContext context) {
        if (required && (value == null || value.isEmpty())) {
            context.buildConstraintViolationWithTemplate("File is required")
                    .addConstraintViolation();
            return false;
        }

        if (value == null) return true;

        final var contentType = value.getContentType();
        for (String mime : mimes) {
            if (StringUtils.endsWith(mime, "/*") &&
                    StringUtils.startsWithIgnoreCase(contentType, mime.substring(0, mimes.length - 2))) {
                return true;
            }

            if (StringUtils.equalsIgnoreCase(contentType, mime)) return true;
        }
        return false;
    }

    public void initialize(MimeType constraintAnnotation) {
        this.required = constraintAnnotation.required();
        this.mimes = constraintAnnotation.value();
    }
}
