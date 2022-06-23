package io.lana.libman.support.data.validate;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanWrapperImpl;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.List;

public class UniqueValidator implements ConstraintValidator<Unique, Object> {
    @PersistenceContext
    private EntityManager entityManager;

    private String[] properties;

    private String id;

    private String[] messages;

    @Override
    public boolean isValid(Object entity, ConstraintValidatorContext context) {
        final var clazz = entity.getClass();
        for (int i = 0; i < properties.length; i++) {
            final var property = properties[i];

            final var query = String.format("from %s where %s != :id and %s = :property",
                    clazz.getName(), id, property);

            final var idValue = new BeanWrapperImpl(entity).getPropertyValue(id);
            final var propertyValue = new BeanWrapperImpl(entity).getPropertyValue(property);
            List<?> list = entityManager.createQuery(query)
                    .setParameter("id", idValue)
                    .setParameter("property", propertyValue)
                    .getResultList();
            if (list == null || list.size() == 0) continue;

            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate(StringUtils.defaultString(messages[i], messages[0]))
                    .addPropertyNode(property)
                    .addConstraintViolation();
            return false;
        }
        return true;
    }


    public void initialize(Unique unique) {
        properties = unique.value();
        id = unique.id();
        messages = unique.messages();
        if (messages.length == 0) {
            messages = new String[properties.length];
            messages[0] = unique.message();
        }
    }
}
