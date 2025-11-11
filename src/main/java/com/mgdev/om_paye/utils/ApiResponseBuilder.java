package com.mgdev.om_paye.utils;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.stereotype.Component;

import com.mgdev.om_paye.dto.response.ApiResponse;
import com.mgdev.om_paye.dto.response.LinksResponse;
import com.mgdev.om_paye.dto.response.PaginationResponse;

import jakarta.servlet.http.HttpServletRequest;

@Component
public class ApiResponseBuilder {

    public <T> ApiResponse<List<T>> buildPagedResponse(Page<T> page, HttpServletRequest request, String message) {
        String baseUrl = request.getRequestURL().toString();

        PaginationResponse pagination = PaginationResponse.builder()
                .currentPage(page.getNumber() + 1)
                .totalPages(page.getTotalPages())
                .totalItems(page.getTotalElements())
                .itemsPerPage(page.getSize())
                .hasNext(page.hasNext())
                .hasPrevious(page.hasPrevious())
                .build();

        LinksResponse links = LinksResponse.builder()
                .self(baseUrl + "?page=" + (page.getNumber() + 1) + "&size=" + page.getSize())
                .next(page.hasNext() ? baseUrl + "?page=" + (page.getNumber() + 2) + "&size=" + page.getSize() : null)
                .previous(page.hasPrevious() ? baseUrl + "?page=" + page.getNumber() + "&size=" + page.getSize() : null)
                .first(baseUrl + "?page=1&size=" + page.getSize())
                .last(baseUrl + "?page=" + page.getTotalPages() + "&size=" + page.getSize())
                .build();

        return ApiResponse.<List<T>>builder()
                .succes(true)
                .message(message)
                .data(page.getContent())
                .pagination(pagination)
                .links(links)
                .build();
    }

public <T> ApiResponse  <T>  buildResponse(T data , String message )
     {
        return ApiResponse.<T>builder()
        .succes(true)
        .message(message)
        .data(data)
        .build();
          
     }
} 

