package io.lana.libman.core.user.role;

import io.lana.libman.support.data.NamedEntity;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Set;
import java.util.stream.Collectors;

import static io.lana.libman.core.user.role.Authorities.*;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Permission extends NamedEntity implements GrantedAuthority {
    private String description;

    @ManyToMany(mappedBy = "permissions")
    private Set<Role> roles = new HashSet<>();

    @Override
    public String getAuthority() {
        return name;
    }

    public static Permission ofName(final String name) {
        final var perm = new Permission();
        perm.id = name;
        perm.name = name;
        perm.createdBy = User.SYSTEM;
        return perm;
    }

    public static List<Permission> builtIns() {
        return List.of(Permission.ofName(ADMIN), Permission.ofName(LIBRARIAN), Permission.ofName(FORCE));
    }

    public static Permission internal() {
        return Permission.ofName(LIBRARIAN);
    }

    public static List<Permission> forReadWrite(final String name) {
        final var nameUpper = StringUtils.upperCase(name, Locale.ENGLISH);
        final var rw = List.of("_CREATE", "_READ", "_DELETE", "_UPDATE");
        return rw.stream().map(action -> Permission.ofName(nameUpper + action))
                .collect(Collectors.toList());
    }

    public static List<Permission> forWriteOnly(final String name) {
        final var nameUpper = StringUtils.upperCase(name, Locale.ENGLISH);
        final var wo = List.of("_CREATE", "_DELETE", "_UPDATE");
        return wo.stream().map(action -> Permission.ofName(nameUpper + action))
                .collect(Collectors.toList());
    }
}
