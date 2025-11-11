package com.mgdev.om_paye.exception;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String resourceName, String field, Object value) {
        super(resourceName + " non trouvé(e) avec " + field + " : " + value);
    }
}
