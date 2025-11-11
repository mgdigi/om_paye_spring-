package com.mgdev.om_paye.exception;

public class PhoneNumberAlreadyUsedException extends RuntimeException {

    public  PhoneNumberAlreadyUsedException(String phoneNumber){
         super("le numero de telephone  " + phoneNumber + " est déjà utilisé ! ");
    }
    
}
