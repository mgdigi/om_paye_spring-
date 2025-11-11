package com.mgdev.om_paye.controller.interceptor;

import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.mgdev.om_paye.controller.interceptor.annotation.ApiResponse;

@RestControllerAdvice
public class ApiResponseAdvice implements ResponseBodyAdvice<Object> {

    @Autowired
    private MessageSource messageSource;

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        // Ne pas appliquer la transformation pour les endpoints Swagger/OpenAPI
        if (returnType.getContainingClass() != null &&
            returnType.getContainingClass().getPackageName() != null &&
            returnType.getContainingClass().getPackageName().startsWith("org.springdoc")) {
            return false;
        }

        return !returnType.getParameterType().equals(com.mgdev.om_paye.dto.response.ApiResponse.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType,
                                  ServerHttpRequest request, ServerHttpResponse response) {

    
        if (body instanceof com.mgdev.om_paye.dto.response.ApiResponse) {
            return body;
        }

    
        ApiResponse annotation = returnType.getMethodAnnotation(ApiResponse.class);
        if (annotation != null && annotation.wrap()) {

            String message = messageSource.getMessage(annotation.messageKey(), null, "Opération réussie", Locale.getDefault());

            return com.mgdev.om_paye.dto.response.ApiResponse.builder()
                    .succes(true)
                    .message(message)
                    .data(body)
                    .build();
        }


        String defaultMessage = messageSource.getMessage("success.default", null, "Opération réussie", Locale.getDefault());
        return com.mgdev.om_paye.dto.response.ApiResponse.builder()
                .succes(true)
                .message(defaultMessage)
                .data(body)
                .build();
    }
}