package io.lana.libman.core.user.role;

import io.lana.libman.support.data.NamedEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping(path = "/authorities/permissions")
class PermissionController extends AuthoritiesCrudController<Permission> {
    protected PermissionController(PermissionRepo repo) {
        super(repo);
    }

    @Override
    protected String getName() {
        return "Permissions";
    }

    @Override
    protected String getAuthority() {
        return "PERMISSION";
    }

    @Override
    protected Collection<? extends NamedEntity> extractRelations(Permission entity) {
        return entity.getRoles();
    }

    @Override
    protected Permission newModelInstance() {
        return new Permission();
    }
}
