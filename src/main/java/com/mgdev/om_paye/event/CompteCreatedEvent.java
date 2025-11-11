package com.mgdev.om_paye.event;

import org.springframework.context.ApplicationEvent;

import com.mgdev.om_paye.entity.Compte;

import lombok.Getter;

@Getter
public class CompteCreatedEvent extends ApplicationEvent {

    private final Compte compte;

    public CompteCreatedEvent(Object source, Compte compte) {
        super(source);
        this.compte = compte;
    }
}