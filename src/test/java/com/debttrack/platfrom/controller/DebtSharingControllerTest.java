package com.debttrack.platfrom.controller;

import com.debttrack.platfrom.service.DebtSharingService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DebtSharingControllerTest {

    @Mock
    private DebtSharingService debtSharingService;

    @InjectMocks
    private DebtSharingController debtSharingController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testDistributeDebt_Success() {
        Long groupId = 1L;
        Double totalAmount = 1000.0;
        Double interestRate = 5.0;
        LocalDate dueDate = LocalDate.now().plusDays(30);

        doNothing().when(debtSharingService).distributeDebt(groupId, totalAmount, interestRate, dueDate);

        ResponseEntity<?> response = debtSharingController.distributeDebt(groupId, totalAmount, interestRate, dueDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Debt distributed evenly among group members", response.getBody());
        verify(debtSharingService, times(1)).distributeDebt(groupId, totalAmount, interestRate, dueDate);
    }

    @Test
    public void testDistributeDebt_InvalidInput() {
        Long groupId = 1L;
        Double totalAmount = 1000.0;
        Double interestRate = 5.0;
        LocalDate dueDate = LocalDate.now().plusDays(30);

        doThrow(new IllegalArgumentException("Invalid input parameters")).when(debtSharingService).distributeDebt(groupId, totalAmount, interestRate, dueDate);

        ResponseEntity<?> response = debtSharingController.distributeDebt(groupId, totalAmount, interestRate, dueDate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input parameters", response.getBody());
        verify(debtSharingService, times(1)).distributeDebt(groupId, totalAmount, interestRate, dueDate);
    }

    @Test
    public void testDistributeDebt_InternalServerError() {
        Long groupId = 1L;
        Double totalAmount = 1000.0;
        Double interestRate = 5.0;
        LocalDate dueDate = LocalDate.now().plusDays(30);

        doThrow(new RuntimeException("Internal server error")).when(debtSharingService).distributeDebt(groupId, totalAmount, interestRate, dueDate);

        ResponseEntity<?> response = debtSharingController.distributeDebt(groupId, totalAmount, interestRate, dueDate);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to distribute debt", response.getBody());
        verify(debtSharingService, times(1)).distributeDebt(groupId, totalAmount, interestRate, dueDate);
    }

    @Test
    public void testDistributeDebtByPercentage_Success() {
        Long groupId = 1L;
        Map<Long, Double> userPercentages = new HashMap<>();
        userPercentages.put(1L, 50.0);
        userPercentages.put(2L, 50.0);
        Double totalAmount = 1000.0;
        Double interestRate = 5.0;
        LocalDate dueDate = LocalDate.now().plusDays(30);

        doNothing().when(debtSharingService).distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);

        ResponseEntity<?> response = debtSharingController.distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Debt distributed by percentage among group members", response.getBody());
        verify(debtSharingService, times(1)).distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);
    }

    @Test
    public void testDistributeDebtByPercentage_InvalidInput() {
        Long groupId = 1L;
        Map<Long, Double> userPercentages = new HashMap<>();
        userPercentages.put(1L, 50.0);
        userPercentages.put(2L, 50.0);
        Double totalAmount = 1000.0;
        Double interestRate = 5.0;
        LocalDate dueDate = LocalDate.now().plusDays(30);

        doThrow(new IllegalArgumentException("Invalid input parameters")).when(debtSharingService).distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);

        ResponseEntity<?> response = debtSharingController.distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid input parameters", response.getBody());
        verify(debtSharingService, times(1)).distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);
    }

    @Test
    public void testDistributeDebtByPercentage_InternalServerError() {
        Long groupId = 1L;
        Map<Long, Double> userPercentages = new HashMap<>();
        userPercentages.put(1L, 50.0);
        userPercentages.put(2L, 50.0);
        Double totalAmount = 1000.0;
        Double interestRate = 5.0;
        LocalDate dueDate = LocalDate.now().plusDays(30);

        doThrow(new RuntimeException("Internal server error")).when(debtSharingService).distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);

        ResponseEntity<?> response = debtSharingController.distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to distribute debt by percentage", response.getBody());
        verify(debtSharingService, times(1)).distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);
    }
}
