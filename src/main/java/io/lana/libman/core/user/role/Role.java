package io.lana.libman.core.user.role;

import io.lana.libman.core.user.User;
import io.lana.libman.support.data.NamedEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

import static io.lana.libman.core.user.role.Authorities.*;
import static io.lana.libman.core.user.role.Authorities.FORCE;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends NamedEntity {
    private String description;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")}
    )
    private Set<Permission> permissions = new HashSet<>();


    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public static Role ofName(String name, Collection<Permission> permissions) {
        final var role = new Role();
        role.id = name;
        role.name = name;
        role.permissions = new HashSet<>(permissions);
        return role;
    }

    public static Role admin() {
        return Role.ofName(ADMIN, Set.of(Permission.ofName(ADMIN), Permission.ofName(LIBRARIAN)));
    }

    public static Role librarian() {
        return Role.ofName(LIBRARIAN, Collections.singleton(Permission.ofName(LIBRARIAN)));
    }

    public static Role force() {
        return Role.ofName(FORCE, Collections.singleton(Permission.ofName(FORCE)));
    }
}
