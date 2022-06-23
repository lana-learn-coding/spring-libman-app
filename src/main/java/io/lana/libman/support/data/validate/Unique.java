package io.lana.libman.support.data.validate;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.Documented;
import java.lang.annotation.Repeatable;
import java.lang.annotation.Retention;
import java.lang.annotation.Target;

import static java.lang.annotation.ElementType.ANNOTATION_TYPE;
import static java.lang.annotation.ElementType.TYPE;
import static java.lang.annotation.RetentionPolicy.RUNTIME;

@Target({ANNOTATION_TYPE, TYPE})
@Documented
@Constraint(validatedBy = {UniqueValidator.class})
@Retention(RUNTIME)
@Repeatable(Unique.List.class)
public @interface Unique {
    String[] value();

    /**
     * Entity class, must specify if validating subclass
     */
    Class<?> entity() default Void.class;

    String id() default "id";

    String message() default "The value was already taken";

    /**
     * Ignore case match when field value is string
     */
    boolean ignoreCase() default true;

    String[] messages() default {};

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};

    @Target({ANNOTATION_TYPE, TYPE})
    @Retention(RUNTIME)
    @Documented
    @interface List {
        Unique[] value();
    }
}
