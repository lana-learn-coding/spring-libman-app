package io.lana.libman.support.data;

import java.lang.reflect.ParameterizedType;

public final class ModelUtils {
    private ModelUtils() {
    }

    public static <T> T construct(Class<T> clazz) {
        try {
            return clazz.getConstructor().newInstance();
        } catch (Exception e) {
            throw new RuntimeException("cannot create object of type: " + clazz.getName(), e);
        }
    }

    public static <T> Class<T> getGenericType(Class<?> clazz) {
        return (Class<T>) ((ParameterizedType) clazz.getGenericSuperclass()).getActualTypeArguments()[0];
    }
}
