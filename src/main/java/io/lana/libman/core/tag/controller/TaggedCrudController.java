package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.TaggedEntity;
import io.lana.libman.core.tag.repo.TaggedRepo;
import io.lana.libman.support.ui.UIFacade;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Locale;
import java.util.Map;


@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
public abstract class TaggedCrudController<T extends TaggedEntity> {
    @Autowired
    protected UIFacade ui;

    protected final TaggedRepo<T> repo;

    protected abstract String getName();

    protected abstract String getAuthority();

    protected String getViewDir() {
        return getName().toLowerCase(Locale.ENGLISH);
    }

    @GetMapping
    public ModelAndView index(@RequestParam(value = "query", required = false) String query, Pageable pageable) {
        final var page = StringUtils.isBlank(query)
                ? repo.findAll(pageable)
                : repo.findAllByNameLike("%" + query + "%", pageable);
        return new ModelAndView("/library/tag/index", Map.of(
                "data", page,
                "title", getName(),
                "authority", getAuthority()
        ));
    }
}
