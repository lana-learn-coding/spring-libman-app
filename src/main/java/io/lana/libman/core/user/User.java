package io.lana.libman.core.user;

import io.lana.libman.core.reader.Reader;
import io.lana.libman.core.user.role.Authorities;
import io.lana.libman.core.user.role.Permission;
import io.lana.libman.core.user.role.Role;
import io.lana.libman.support.data.AuditableEntity;
import io.lana.libman.support.data.Gender;
import io.lana.libman.support.data.Named;
import io.lana.libman.support.security.AuthUser;
import io.lana.libman.support.validate.DateBeforeNow;
import io.lana.libman.support.validate.PhoneNumber;
import io.lana.libman.support.validate.StrongPassword;
import io.lana.libman.support.validate.Unique;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.apache.commons.lang3.StringUtils;

import javax.persistence.*;
import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.time.LocalDate;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

@Setter
@Getter
@Entity
@Table(name = "usr")
@NoArgsConstructor
@Unique(value = "email", message = "The email was already registered")
@Unique(value = "phone", message = "The phone was already used")
public class User extends AuditableEntity implements AuthUser, Named {
    @Email
    @NotBlank
    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false, unique = true)
    private String username;

    @StrongPassword
    private String password;

    private String avatar;

    @NotBlank
    @Column(name = "first_name")
    private String firstName = "Anon";

    @Column(name = "last_name")
    private String lastName;

    @NotBlank
    @PhoneNumber
    private String phone;

    private String address;

    @NotNull
    @Enumerated
    private Gender gender = Gender.UNSPECIFIED;

    @OneToOne(mappedBy = "account")
    private Reader reader;

    @DateBeforeNow
    @Column(name = "date_of_birth")
    private LocalDate dateOfBirth;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = {@JoinColumn(name = "user_id", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (user_id) REFERENCES usr(id) ON DELETE CASCADE"))},
            inverseJoinColumns = {@JoinColumn(name = "role_id", foreignKey = @ForeignKey(foreignKeyDefinition = "FOREIGN KEY (role_id) REFERENCES role(id) ON DELETE CASCADE"))}
    )
    private Set<Role> roles = new HashSet<>();


    @Getter(AccessLevel.NONE)
    @Transient
    private Set<Permission> permissions;

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

    public boolean isInternal() {
        return getAuthorities().contains(Permission.internal());
    }

    public boolean isReader() {
        return reader != null;
    }

    public void setEmail(String email) {
        this.email = email;
        if (StringUtils.isEmpty(username)) this.username = email;
    }

    @Override
    public String getUsername() {
        return StringUtils.defaultIfEmpty(username, email);
    }

    @Transient
    @Override
    public String getName() {
        return getUsername();
    }

    @Transient
    @Override
    public void setName(String name) {
        this.username = name;
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

    public static User newInstance(String email, String username, String password, Collection<Role> roles, String createdBy) {
        final var user = newInstance(email, username, password, roles);
        user.setCreatedBy(Authorities.User.SYSTEM);
        return user;
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
