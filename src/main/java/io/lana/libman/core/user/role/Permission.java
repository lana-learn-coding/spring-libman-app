package io.lana.libman.core.user.role;

import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.data.Named;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.ManyToMany;
import java.util.HashSet;
import java.util.Set;

import static io.lana.libman.core.user.role.Authorities.ADMIN;
import static io.lana.libman.core.user.role.Authorities.LIBRARIAN;

@Getter
@Setter
@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Permission extends AuditableEntity implements GrantedAuthority, Named {
    private String name;

    private String description;

    @ManyToMany(mappedBy = "permissions", fetch = FetchType.EAGER)
    private Set<Role> roles = new HashSet<>();

    @Override
    public String getAuthority() {
        return name;
    }

    public static Permission ofName(String name) {
        final var perm = new Permission();
        perm.name = name;
        return perm;
    }

    public static Permission admin() {
        final var perm = Permission.ofName(ADMIN);
        perm.id = "01G54DF50000359SJM3WAPSDAA";
        return perm;
    }

    public static Permission librarian() {
        final var perm = Permission.ofName(LIBRARIAN);
        perm.id = "01G54DHD6W27QM73DGNE58381V";
        return perm;
    }
}
