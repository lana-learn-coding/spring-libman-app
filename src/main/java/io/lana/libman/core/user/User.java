package io.lana.libman.core.user;

import io.lana.libman.core.user.role.Permission;
import io.lana.libman.core.user.role.Role;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.data.Gender;
import io.lana.libman.support.security.AuthUser;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.core.GrantedAuthority;

import javax.persistence.*;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table(name = "`user`")
@NoArgsConstructor
public class User extends AuditableEntity implements AuthUser {
    private String email;

    private String username;

    private String password;

    private String avatar;

    @Column(name = "first_name")
    private String firstName = "Anon";

    @Column(name = "last_name")
    private String lastName;

    private String phone;

    private String address;

    @Enumerated
    private Gender gender = Gender.UNSPECIFIED;

    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private Set<Role> roles = new HashSet<>();


    @Getter(AccessLevel.NONE)
    @Transient
    private Set<Permission> permissions;

    @Transient
    public boolean isInternal() {
        return getAuthorities().stream().anyMatch(p -> p.equals(Permission.librarian()));
    }

    @Transient
    @Override
    public Collection<Permission> getAuthorities() {
        if (permissions == null) {
            permissions = roles.stream()
                    .flatMap(role -> role.getPermissions().stream())
                    .collect(Collectors.toUnmodifiableSet());
        }
        return permissions;
    }

    @Transient
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Transient
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Transient
    @Override
    public boolean isEnabled() {
        return true;
    }

    public static User newInstance(String email, String username, String password, Collection<Role> roles) {
        final var user = new User();
        user.email = email;
        user.username = username;
        user.password = password;
        user.roles = new HashSet<>(roles);
        return user;
    }
}
