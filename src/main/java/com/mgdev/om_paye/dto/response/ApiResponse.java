package com.mgdev.om_paye.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class ApiResponse<T> {
   
    private Boolean succes;
    private String message;
    private T data;
    private PaginationResponse pagination;
    private LinksResponse links;

}
