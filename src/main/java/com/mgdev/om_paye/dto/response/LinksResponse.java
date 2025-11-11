package com.mgdev.om_paye.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * Contient les liens HATEOAS pour naviguer entre les pages.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class LinksResponse {
    private String self;
    private String next;
    private String previous;
    private String first;
    private String last;
}
