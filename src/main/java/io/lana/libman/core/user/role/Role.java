package io.lana.libman.core.user.role;

import io.lana.libman.core.user.User;
import io.lana.libman.support.data.DescriptiveEntity;
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

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Role extends DescriptiveEntity {
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "role_permission",
            joinColumns = {@JoinColumn(name = "role_id")},
            inverseJoinColumns = {@JoinColumn(name = "permission_id")},
            foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE"),
            inverseForeignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (permission_id) REFERENCES perm(id) ON DELETE CASCADE")
    )
    private Set<Permission> permissions = new HashSet<>();


    @ManyToMany(mappedBy = "roles")
    private Set<User> users = new HashSet<>();

    public static Role ofName(String name, Collection<Permission> permissions) {
        final var role = new Role();
        role.id = name;
        role.name = name;
        role.permissions = new HashSet<>(permissions);
        role.createdBy = Authorities.User.SYSTEM;
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
