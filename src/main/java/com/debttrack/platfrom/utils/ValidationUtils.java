package com.debttrack.platfrom.utils;

import com.debttrack.platfrom.model.DebtRequest;

import java.time.LocalDate;

public class ValidationUtils {
    public static void validateDebtRequest(DebtRequest request) {
        if (request.getAmount() <= 0) {
            throw new IllegalArgumentException("Amount must be greater than 0");
        }
        if (request.getInterestRate() < 0) {
            throw new IllegalArgumentException("Interest rate cannot be negative");
        }
        if (request.getDueDate().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Due date must be in the future");
        }
    }
}