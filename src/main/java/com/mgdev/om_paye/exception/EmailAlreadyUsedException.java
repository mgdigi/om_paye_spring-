package com.mgdev.om_paye.exception;

public class EmailAlreadyUsedException extends RuntimeException {
     public EmailAlreadyUsedException(String email) {
        super("L'email " + email + " est déjà utilisé ! ");
    }
}
