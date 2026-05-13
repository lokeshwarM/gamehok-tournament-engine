package com.gamehok.tournament.util;

import org.springframework.data.domain.Page;
import com.gamehok.tournament.common.dto.PageResponse;

import java.util.List;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Utility for converting Spring Data {@link Page} to {@link PageResponse} DTOs.
 */
public final class PageUtils {

    private PageUtils() {}

    public static <S, T> PageResponse<T> toPageResponse(Page<S> page, Function<S, T> mapper) {
        List<T> content = page.getContent().stream()
                .map(mapper)
                .collect(Collectors.toList());

        return PageResponse.<T>builder()
                .content(content)
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .first(page.isFirst())
                .last(page.isLast())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();
    }
}
