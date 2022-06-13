package io.lana.libman.config;

import com.github.javafaker.Faker;
import io.lana.libman.core.tag.*;
import io.lana.libman.core.tag.repo.*;
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
import java.time.ZoneId;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
class InitialDataConfig implements ApplicationRunner {
    private final UserRepo userRepo;

    private final PermissionRepo permissionRepo;

    private final RoleRepo roleRepo;

    private final AuthorRepo authorRepo;

    private final PublisherRepo publisherRepo;

    private final GenreRepo genreRepo;

    private final SeriesRepo seriesRepo;

    private final ShelfRepo shelfRepo;

    private final PasswordEncoder passwordEncoder;


    @Override
    public void run(ApplicationArguments args) throws Exception {
        initPermission();
        initUser();
        initTag();
    }

    @Transactional
    void initPermission() {
        if (permissionRepo.count() == 0) {
            permissionRepo.saveAll(Permission.builtIns());
            permissionRepo.saveAll(Permission.forWriteOnly(Author.class.getSimpleName()));
            permissionRepo.saveAll(Permission.forWriteOnly(Genre.class.getSimpleName()));
            permissionRepo.saveAll(Permission.forWriteOnly(Publisher.class.getSimpleName()));
            permissionRepo.saveAll(Permission.forWriteOnly(Series.class.getSimpleName()));
            permissionRepo.saveAll(Permission.forWriteOnly(Shelf.class.getSimpleName()));
            permissionRepo.saveAll(Permission.forReadWrite(User.class.getSimpleName()));
        }

        if (roleRepo.count() == 0) {
            roleRepo.saveAll(List.of(Role.admin(), Role.librarian(), Role.force()));
        }
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

    @Transactional
    void initTag() {
        var faker = Faker.instance();
        if (authorRepo.count() == 0) {
            Stream.generate(() -> faker.book().author())
                    .distinct()
                    .limit(20)
                    .forEach(name -> {
                        final var author = new Author();
                        author.setName(name);
                        author.setAbout(faker.superhero().descriptor());
                        author.setDateOfBirth(faker.date().birthday(20, 65).toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        if (faker.bool().bool()) {
                            author.setDateOfDeath(faker.date().past(36500, 29000, TimeUnit.DAYS)
                                    .toInstant().atZone(ZoneId.systemDefault()).toLocalDate());
                        }
                        authorRepo.save(author);
                    });
        }

        if (genreRepo.count() == 0) {
            Stream.generate(() -> faker.book().genre())
                    .distinct()
                    .limit(10)
                    .forEach(name -> {
                        final var genres = new Genre();
                        genres.setName(name);
                        genres.setAbout(faker.weather().description());
                        genreRepo.save(genres);
                    });
        }

        if (publisherRepo.count() == 0) {
            Stream.generate(() -> faker.book().publisher())
                    .distinct()
                    .limit(8)
                    .forEach(name -> {
                        final var publisher = new Publisher();
                        publisher.setName(name);
                        publisher.setAbout(faker.weather().description());
                        publisherRepo.save(publisher);
                    });
        }

        if (seriesRepo.count() == 0) {
            Stream.generate(() -> faker.company().catchPhrase())
                    .distinct()
                    .limit(10)
                    .forEach(name -> {
                        final var series = new Series();
                        series.setName(name);
                        series.setAbout(faker.howIMetYourMother().catchPhrase());
                        seriesRepo.save(series);
                    });
        }

        if (shelfRepo.count() == 0) {
            shelfRepo.save(Shelf.storage());
            shelfRepo.save(Shelf.ofName("Historical"));
            shelfRepo.save(Shelf.ofName("Science"));
            shelfRepo.save(Shelf.ofName("Manga"));
            shelfRepo.save(Shelf.ofName("Travel"));
            shelfRepo.save(Shelf.ofName("Self-help / Personal"));
        }
    }
}
