package io.lana.libman.core.tag.controller;

import io.lana.libman.core.tag.TaggedEntity;
import io.lana.libman.core.tag.repo.TaggedRepo;
import lombok.AccessLevel;
import lombok.RequiredArgsConstructor;
import org.apache.commons.lang3.StringUtils;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.ModelAndView;

import java.util.Map;


@RequiredArgsConstructor(access = AccessLevel.PROTECTED)
abstract class TaggedCrudController<T extends TaggedEntity> {
    protected final TaggedRepo<T> repo;

    protected abstract String getViewDir();

    @GetMapping
    public ModelAndView index(@RequestParam(value = "query", required = false) String query, Pageable pageable) {
        final var page = StringUtils.isBlank(query)
                ? repo.findAll(pageable)
                : repo.findAllByNameLike(query, pageable);
        return new ModelAndView("/library" + getViewDir() + "/index", Map.of("data", page));
    }
}
