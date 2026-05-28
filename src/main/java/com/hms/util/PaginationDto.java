package com.hms.util;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Pagination metadata wrapper
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class PaginationDto {
    private int currentPage;
    private int pageSize;
    private long totalRecords;
    private int totalPages;
    private boolean hasNext;
    private boolean hasPrevious;
}
