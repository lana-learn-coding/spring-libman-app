package io.lana.libman.config;

import io.lana.libman.core.user.User;
import io.lana.libman.core.user.UserRepo;
import io.lana.libman.core.user.role.Permission;
import io.lana.libman.core.user.role.PermissionRepo;
import io.lana.libman.core.user.role.Role;
import io.lana.libman.core.user.role.RoleRepo;
import lombok.RequiredArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.transaction.Transactional;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
class InitialDataConfig implements ApplicationRunner {
    private final UserRepo userRepo;

    private final PermissionRepo permissionRepo;

    private final RoleRepo roleRepo;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initPermission();
        initRole();
        initUser();
    }

    @Transactional
    void initPermission() {
        if (permissionRepo.count() > 0) return;
        permissionRepo.saveAll(List.of(Permission.admin(), Permission.librarian()));
    }

    @Transactional
    void initRole() {
        if (roleRepo.count() > 0) return;
        roleRepo.saveAll(List.of(Role.admin(), Role.librarian()));
    }

    @Transactional
    void initUser() {
        if (userRepo.count() > 0) return;

        final var password = passwordEncoder.encode("1");

        userRepo.saveAll(List.of(
                User.newInstance("admin@admin", "admin", password, Collections.singleton(Role.admin())),
                User.newInstance("librarian@librarian", "librarian", password, Collections.singleton(Role.librarian())),
                User.newInstance("reader@reader", "reader", password, Collections.emptyList())
        ));
    }

}
