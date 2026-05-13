package com.gamehok.tournament.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

/**
 * Paginated response wrapper for list endpoints.
 * <p>
 * Wraps paginated query results with cursor/offset metadata.
 * Always prefer this over raw lists for collection endpoints.
 * </p>
 *
 * @param <T> the type of each element in the page
 */
@Getter
@Builder
public class PageResponse<T> {

    private final List<T> content;
    private final int page;
    private final int size;
    private final long totalElements;
    private final int totalPages;
    private final boolean first;
    private final boolean last;
    private final boolean hasNext;
    private final boolean hasPrevious;
}
