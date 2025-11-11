package com.mgdev.om_paye.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class PaginationResponse {
    private int currentPage;
    private int totalPages;
    private long totalItems;
    private int itemsPerPage;
    private boolean hasNext;
    private boolean hasPrevious;
}
