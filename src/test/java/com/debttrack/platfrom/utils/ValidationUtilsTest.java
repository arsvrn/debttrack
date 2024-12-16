package com.debttrack.platfrom.utils;

import com.debttrack.platfrom.model.DebtRequest;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

public class ValidationUtilsTest {

    @Test
    public void testValidateDebtRequest_AmountLessThanOrEqualToZero() {
        DebtRequest request = new DebtRequest();
        request.setAmount(-1.0);
        request.setInterestRate(5.0);
        request.setDueDate(LocalDate.now().plusDays(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtils.validateDebtRequest(request);
        });

        assertEquals("Amount must be greater than 0", exception.getMessage());
    }

    @Test
    public void testValidateDebtRequest_NegativeInterestRate() {
        DebtRequest request = new DebtRequest();
        request.setAmount(1000.0);
        request.setInterestRate(-5.0);
        request.setDueDate(LocalDate.now().plusDays(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtils.validateDebtRequest(request);
        });

        assertEquals("Interest rate cannot be negative", exception.getMessage());
    }

    @Test
    public void testValidateDebtRequest_DueDateInThePast() {
        DebtRequest request = new DebtRequest();
        request.setAmount(1000.0);
        request.setInterestRate(5.0);
        request.setDueDate(LocalDate.now().minusDays(1));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            ValidationUtils.validateDebtRequest(request);
        });

        assertEquals("Due date must be in the future", exception.getMessage());
    }

    @Test
    public void testValidateDebtRequest_ValidRequest() {
        DebtRequest request = new DebtRequest();
        request.setAmount(1000.0);
        request.setInterestRate(5.0);
        request.setDueDate(LocalDate.now().plusDays(1));

        ValidationUtils.validateDebtRequest(request);
    }
}
