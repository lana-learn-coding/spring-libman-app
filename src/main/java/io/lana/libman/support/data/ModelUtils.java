package io.lana.libman.support.data;

import org.springframework.beans.BeanUtils;

import java.lang.reflect.ParameterizedType;

public final class ModelUtils {
    private ModelUtils() {
    }

    public static <T> T construct(Class<T> clazz) {
        return BeanUtils.instantiateClass(clazz);
    }

    public static <T> Class<T> getGenericType(Class<?> clazz) {
        return (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
