package com.debttrack.platfrom.utils;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest
public class DebtCalculatorTest {

    @Autowired
    private DebtCalculator debtCalculator;


    @Test
    public void testCalculateTotalAmount() {
        Double amount = 1000.0;
        Double interestRate = 10.0;
        Double expectedTotalAmount = 1100.0;

        Double actualTotalAmount = debtCalculator.calculateTotalAmount(amount, interestRate);

        assertEquals(expectedTotalAmount, actualTotalAmount, "Итоговая сумма долга рассчитана неверно");
    }

    @Test
    public void testCalculatePenalty() {
        Double amount = 1000.0;
        long overdueDays = 5;
        Double penaltyRatePerDay = 2.0;
        Double expectedPenalty = 100.0;

        Double actualPenalty = debtCalculator.calculatePenalty(amount, overdueDays, penaltyRatePerDay);

        assertEquals(expectedPenalty, actualPenalty, "Штраф за просрочку рассчитан неверно");
    }
}
