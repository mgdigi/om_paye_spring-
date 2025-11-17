package com.mgdev.om_paye.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.mgdev.om_paye.dto.response.ApiResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(ResourceNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleResourceNotFoundException(ResourceNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSucces(false);
        response.setMessage(ex.getMessage());
        response.setData(null);
        response.setPagination(null);
        response.setLinks(null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ApiResponse<Object>> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSucces(false);
        response.setMessage(ex.getMessage());
        response.setData(null);
        response.setPagination(null);
        response.setLinks(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(PhoneNumberAlreadyUsedException.class)
    public ResponseEntity<ApiResponse<Object>> handlePhoneNumberAlreadyUsedException(PhoneNumberAlreadyUsedException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSucces(false);
        response.setMessage(ex.getMessage());
        response.setData(null);
        response.setPagination(null);
        response.setLinks(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<ApiResponse<Object>> handleIllegalArgumentException(IllegalArgumentException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSucces(false);
        response.setMessage(ex.getMessage());
        response.setData(null);
        response.setPagination(null);
        response.setLinks(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserNotFoundException(UserNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSucces(false);
        response.setMessage(ex.getMessage());
        response.setData(null);
        response.setPagination(null);
        response.setLinks(null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(UserHasNoAccountException.class)
    public ResponseEntity<ApiResponse<Object>> handleUserHasNoAccountException(UserHasNoAccountException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSucces(false);
        response.setMessage(ex.getMessage());
        response.setData(null);
        response.setPagination(null);
        response.setLinks(null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(InsufficientBalanceException.class)
    public ResponseEntity<ApiResponse<Object>> handleInsufficientBalanceException(InsufficientBalanceException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSucces(false);
        response.setMessage(ex.getMessage());
        response.setData(null);
        response.setPagination(null);
        response.setLinks(null);
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }

    @ExceptionHandler(RecipientNotFoundException.class)
    public ResponseEntity<ApiResponse<Object>> handleRecipientNotFoundException(RecipientNotFoundException ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSucces(false);
        response.setMessage(ex.getMessage());
        response.setData(null);
        response.setPagination(null);
        response.setLinks(null);
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(response);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ApiResponse<Object>> handleGenericException(Exception ex) {
        ApiResponse<Object> response = new ApiResponse<>();
        response.setSucces(false);
        response.setMessage("Une erreur inattendue s'est produite");
        response.setData(null);
        response.setPagination(null);
        response.setLinks(null);
        return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(response);
    }
}
