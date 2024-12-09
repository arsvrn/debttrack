package com.debttrack.platfrom.controller;

import com.debttrack.platfrom.model.Debt;
import com.debttrack.platfrom.model.DebtRequest;
import com.debttrack.platfrom.service.DebtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DebtControllerTest {

    @Mock
    private DebtService debtService;

    @InjectMocks
    private DebtController debtController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddDebt_Success() {
        DebtRequest request = new DebtRequest();
        Debt debt = new Debt();
        when(debtService.addDebt(request)).thenReturn(debt);

        ResponseEntity<?> response = debtController.addDebt(request);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
        assertEquals(debt, response.getBody());
        verify(debtService, times(1)).addDebt(request);
    }

    @Test
    public void testAddDebt_InvalidRequest() {
        DebtRequest request = new DebtRequest();
        when(debtService.addDebt(request)).thenThrow(new IllegalArgumentException("Invalid debt request"));

        ResponseEntity<?> response = debtController.addDebt(request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid debt request", response.getBody());
        verify(debtService, times(1)).addDebt(request);
    }

    @Test
    public void testAddDebt_InternalServerError() {
        DebtRequest request = new DebtRequest();
        when(debtService.addDebt(request)).thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<?> response = debtController.addDebt(request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to add debt", response.getBody());
        verify(debtService, times(1)).addDebt(request);
    }

    @Test
    public void testUpdateDebt_Success() {
        Long debtId = 1L;
        DebtRequest request = new DebtRequest();
        Debt debt = new Debt();
        when(debtService.updateDebt(debtId, request)).thenReturn(debt);

        ResponseEntity<?> response = debtController.updateDebt(debtId, request);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(debt, response.getBody());
        verify(debtService, times(1)).updateDebt(debtId, request);
    }

    @Test
    public void testUpdateDebt_InvalidRequest() {
        Long debtId = 1L;
        DebtRequest request = new DebtRequest();
        when(debtService.updateDebt(debtId, request)).thenThrow(new IllegalArgumentException("Invalid debt request"));

        ResponseEntity<?> response = debtController.updateDebt(debtId, request);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertEquals("Invalid debt request", response.getBody());
        verify(debtService, times(1)).updateDebt(debtId, request);
    }

    @Test
    public void testUpdateDebt_InternalServerError() {
        Long debtId = 1L;
        DebtRequest request = new DebtRequest();
        when(debtService.updateDebt(debtId, request)).thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<?> response = debtController.updateDebt(debtId, request);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to update debt", response.getBody());
        verify(debtService, times(1)).updateDebt(debtId, request);
    }

    @Test
    public void testDeleteDebt_Success() {
        Long debtId = 1L;
        doNothing().when(debtService).deleteDebt(debtId);

        ResponseEntity<?> response = debtController.deleteDebt(debtId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Debt deleted successfully", response.getBody());
        verify(debtService, times(1)).deleteDebt(debtId);
    }

    @Test
    public void testDeleteDebt_DebtNotFound() {
        Long debtId = 1L;
        doThrow(new IllegalArgumentException("Debt not found")).when(debtService).deleteDebt(debtId);

        ResponseEntity<?> response = debtController.deleteDebt(debtId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Debt not found", response.getBody());
        verify(debtService, times(1)).deleteDebt(debtId);
    }

    @Test
    public void testDeleteDebt_InternalServerError() {
        Long debtId = 1L;
        doThrow(new RuntimeException("Internal server error")).when(debtService).deleteDebt(debtId);

        ResponseEntity<?> response = debtController.deleteDebt(debtId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to delete debt", response.getBody());
        verify(debtService, times(1)).deleteDebt(debtId);
    }

    @Test
    public void testGetDebtsForCreditor_Success() {
        Long creditorId = 1L;
        List<Debt> debts = Arrays.asList(new Debt(), new Debt());
        when(debtService.getDebtsForCreditor(creditorId)).thenReturn(debts);

        ResponseEntity<?> response = debtController.getDebtsForCreditor(creditorId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(debts, response.getBody());
        verify(debtService, times(1)).getDebtsForCreditor(creditorId);
    }

    @Test
    public void testGetDebtsForCreditor_CreditorNotFound() {
        Long creditorId = 1L;
        when(debtService.getDebtsForCreditor(creditorId)).thenThrow(new IllegalArgumentException("Creditor not found"));

        ResponseEntity<?> response = debtController.getDebtsForCreditor(creditorId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Creditor not found", response.getBody());
        verify(debtService, times(1)).getDebtsForCreditor(creditorId);
    }

    @Test
    public void testGetDebtsForCreditor_InternalServerError() {
        Long creditorId = 1L;
        when(debtService.getDebtsForCreditor(creditorId)).thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<?> response = debtController.getDebtsForCreditor(creditorId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to retrieve debts for creditor", response.getBody());
        verify(debtService, times(1)).getDebtsForCreditor(creditorId);
    }

    @Test
    public void testGetDebtsForBorrower_Success() {
        Long borrowerId = 1L;
        List<Debt> debts = Arrays.asList(new Debt(), new Debt());
        when(debtService.getDebtsForBorrower(borrowerId)).thenReturn(debts);

        ResponseEntity<?> response = debtController.getDebtsForBorrower(borrowerId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(debts, response.getBody());
        verify(debtService, times(1)).getDebtsForBorrower(borrowerId);
    }

    @Test
    public void testGetDebtsForBorrower_BorrowerNotFound() {
        Long borrowerId = 1L;
        when(debtService.getDebtsForBorrower(borrowerId)).thenThrow(new IllegalArgumentException("Borrower not found"));

        ResponseEntity<?> response = debtController.getDebtsForBorrower(borrowerId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Borrower not found", response.getBody());
        verify(debtService, times(1)).getDebtsForBorrower(borrowerId);
    }

    @Test
    public void testGetDebtsForBorrower_InternalServerError() {
        Long borrowerId = 1L;
        when(debtService.getDebtsForBorrower(borrowerId)).thenThrow(new RuntimeException("Internal server error"));

        ResponseEntity<?> response = debtController.getDebtsForBorrower(borrowerId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to retrieve debts for borrower", response.getBody());
        verify(debtService, times(1)).getDebtsForBorrower(borrowerId);
    }

    @Test
    public void testCalculateDebt_Success() {
        Long debtId = 1L;
        doNothing().when(debtService).calculateDebtTotals(debtId);

        ResponseEntity<?> response = debtController.calculateDebt(debtId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Debt totals calculated", response.getBody());
        verify(debtService, times(1)).calculateDebtTotals(debtId);
    }

    @Test
    public void testCalculateDebt_DebtNotFound() {
        Long debtId = 1L;
        doThrow(new IllegalArgumentException("Debt not found")).when(debtService).calculateDebtTotals(debtId);

        ResponseEntity<?> response = debtController.calculateDebt(debtId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Debt not found", response.getBody());
        verify(debtService, times(1)).calculateDebtTotals(debtId);
    }

    @Test
    public void testCalculateDebt_InternalServerError() {
        Long debtId = 1L;
        doThrow(new RuntimeException("Internal server error")).when(debtService).calculateDebtTotals(debtId);

        ResponseEntity<?> response = debtController.calculateDebt(debtId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to calculate debt totals", response.getBody());
        verify(debtService, times(1)).calculateDebtTotals(debtId);
    }

    @Test
    public void testCloseDebt_Success() {
        Long debtId = 1L;
        doNothing().when(debtService).markAsPaid(debtId);

        ResponseEntity<?> response = debtController.closeDebt(debtId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Debt marked as paid", response.getBody());
        verify(debtService, times(1)).markAsPaid(debtId);
    }

    @Test
    public void testCloseDebt_DebtNotFound() {
        Long debtId = 1L;
        doThrow(new IllegalArgumentException("Debt not found")).when(debtService).markAsPaid(debtId);

        ResponseEntity<?> response = debtController.closeDebt(debtId);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Debt not found", response.getBody());
        verify(debtService, times(1)).markAsPaid(debtId);
    }

    @Test
    public void testCloseDebt_InternalServerError() {
        Long debtId = 1L;
        doThrow(new RuntimeException("Internal server error")).when(debtService).markAsPaid(debtId);

        ResponseEntity<?> response = debtController.closeDebt(debtId);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        assertEquals("Failed to mark debt as paid", response.getBody());
        verify(debtService, times(1)).markAsPaid(debtId);
    }
}
