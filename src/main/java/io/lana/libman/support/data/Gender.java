package io.lana.libman.support.data;

import lombok.Getter;

@Getter
public enum Gender {
    UNSPECIFIED(0, "Unspecified"),
    MALE(1, "Male"),
    FEMALE(2, "Female"),
    ATTACK_HELICOPTER(3, "Attack Helicopter");

    private final String name;

    private final int id;

    Gender(int id, String name) {
        this.id = id;
        this.name = name;
    }
}
