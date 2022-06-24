package io.lana.libman.core.user.role;

import io.lana.libman.support.data.NamedEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Collection;

@Controller
@RequestMapping(path = "/authorities/roles")
class RoleController extends AuthoritiesCrudController<Role> {
    protected RoleController(RoleRepo repo) {
        super(repo);
    }

    @Override
    protected String getName() {
        return "Roles";
    }

    @Override
    protected String getAuthority() {
        return "ROLE";
    }

    @Override
    protected Collection<? extends NamedEntity> extractRelations(Role entity) {
        return entity.getPermissions();
    }

    @Override
    protected String getEditFormView() {
        return "/auth/role/role-edit";
    }

    @Override
    protected Role newModelInstance() {
        return new Role();
    }
}
