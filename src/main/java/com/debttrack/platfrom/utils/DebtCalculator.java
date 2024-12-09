package com.debttrack.platfrom.utils;

import org.springframework.stereotype.Component;

@Component
public class DebtCalculator {

    /**
     * Рассчитывает итоговую сумму долга (с процентами).
     */
    public Double calculateTotalAmount(Double amount, Double interestRate) {
        return amount + (amount * (interestRate / 100));
    }

    /**
     * Рассчитывает штраф за просрочку.
     */
    public Double calculatePenalty(Double amount, long overdueDays, Double penaltyRatePerDay) {
        return amount * (penaltyRatePerDay / 100) * overdueDays;
    }
}