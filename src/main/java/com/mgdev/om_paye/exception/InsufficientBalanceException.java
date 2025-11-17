package com.mgdev.om_paye.exception;

import java.math.BigDecimal;

public class InsufficientBalanceException extends RuntimeException {
    private final BigDecimal currentBalance;
    private final BigDecimal requiredAmount;

    public InsufficientBalanceException(BigDecimal currentBalance, BigDecimal requiredAmount) {
        super(String.format("Solde insuffisant. Solde actuel: %s, Montant requis: %s",
              currentBalance, requiredAmount));
        this.currentBalance = currentBalance;
        this.requiredAmount = requiredAmount;
    }

    public InsufficientBalanceException(String message) {
        super(message);
        this.currentBalance = null;
        this.requiredAmount = null;
    }

    public BigDecimal getCurrentBalance() {
        return currentBalance;
    }

    public BigDecimal getRequiredAmount() {
        return requiredAmount;
    }
}