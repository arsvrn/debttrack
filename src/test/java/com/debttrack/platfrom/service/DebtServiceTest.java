package com.debttrack.platfrom.service;

import com.debttrack.platfrom.enums.DebtStatus;
import com.debttrack.platfrom.model.Debt;
import com.debttrack.platfrom.model.DebtRequest;
import com.debttrack.platfrom.model.User;
import com.debttrack.platfrom.repository.DebtRepository;
import com.debttrack.platfrom.repository.UserRepository;
import com.debttrack.platfrom.utils.DebtCalculator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

public class DebtServiceTest {

    @Mock
    private DebtRepository debtRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private DebtCalculator debtCalculator;

    @InjectMocks
    private DebtService debtService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testAddDebt() {
        DebtRequest request = new DebtRequest();
        request.setCreditorId(1L);
        request.setBorrowerId(2L);
        request.setAmount(1000.0);
        request.setInterestRate(5.0);
        request.setDueDate(LocalDate.now().plusDays(30));
        request.setNote("Test debt");
        request.setPenaltyAmount(1.0);

        User creditor = new User();
        creditor.setId(1L);
        User borrower = new User();
        borrower.setId(2L);

        when(userRepository.findById(1L)).thenReturn(Optional.of(creditor));
        when(userRepository.findById(2L)).thenReturn(Optional.of(borrower));

        Debt debt = new Debt();
        debt.setCreditor(creditor);
        debt.setBorrower(borrower);
        debt.setAmount(request.getAmount());
        debt.setInterestRate(request.getInterestRate());
        debt.setDueDate(request.getDueDate());
        debt.setNote(request.getNote());
        debt.setStatus(DebtStatus.ACTIVE);
        debt.setPenaltyAmount(request.getPenaltyAmount());

        when(debtRepository.save(any(Debt.class))).thenReturn(debt);

        Debt result = debtService.addDebt(request);

        assertEquals(debt, result);
    }

    @Test
    public void testUpdateDebt() {
        Long debtId = 1L;
        DebtRequest request = new DebtRequest();
        request.setAmount(1500.0);
        request.setInterestRate(7.0);
        request.setDueDate(LocalDate.now().plusDays(45));
        request.setNote("Updated debt");

        Debt debt = new Debt();
        debt.setId(debtId);
        debt.setAmount(request.getAmount());
        debt.setInterestRate(request.getInterestRate());
        debt.setDueDate(request.getDueDate());
        debt.setNote(request.getNote());

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));
        when(debtRepository.save(any(Debt.class))).thenReturn(debt);

        Debt result = debtService.updateDebt(debtId, request);

        assertEquals(debt, result);
    }

    @Test
    public void testDeleteDebt() {
        Long debtId = 1L;
        Debt debt = new Debt();
        debt.setId(debtId);

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));

        debtService.deleteDebt(debtId);

        verify(debtRepository, times(1)).delete(debt);
    }

    @Test
    public void testGetDebtsForCreditor() {
        Long creditorId = 1L;
        User creditor = new User();
        creditor.setId(creditorId);

        Debt debt1 = new Debt();
        debt1.setCreditor(creditor);
        Debt debt2 = new Debt();
        debt2.setCreditor(creditor);

        when(userRepository.findById(creditorId)).thenReturn(Optional.of(creditor));
        when(debtRepository.findByCreditor(creditor)).thenReturn(Arrays.asList(debt1, debt2));

        List<Debt> result = debtService.getDebtsForCreditor(creditorId);

        assertEquals(2, result.size());
    }

    @Test
    public void testGetDebtsForBorrower() {
        Long borrowerId = 2L;
        User borrower = new User();
        borrower.setId(borrowerId);

        Debt debt1 = new Debt();
        debt1.setBorrower(borrower);
        Debt debt2 = new Debt();
        debt2.setBorrower(borrower);

        when(userRepository.findById(borrowerId)).thenReturn(Optional.of(borrower));
        when(debtRepository.findByBorrower(borrower)).thenReturn(Arrays.asList(debt1, debt2));

        List<Debt> result = debtService.getDebtsForBorrower(borrowerId);

        assertEquals(2, result.size());
    }

    @Test
    public void testCalculateDebtTotals() {
        Long debtId = 1L;
        Debt debt = new Debt();
        debt.setId(debtId);
        debt.setAmount(1000.0);
        debt.setInterestRate(5.0);
        debt.setDueDate(LocalDate.now().minusDays(10));
        debt.setPenaltyAmount(1.0);
        debt.setStatus(DebtStatus.ACTIVE);

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));
        when(debtCalculator.calculateTotalAmount(debt.getAmount(), debt.getInterestRate())).thenReturn(1050.0);
        when(debtCalculator.calculatePenalty(debt.getAmount(), 10, debt.getPenaltyAmount())).thenReturn(100.0);

        debtService.calculateDebtTotals(debtId);

        verify(debtRepository, times(1)).save(debt);
        assertEquals(1050.0, debt.getTotalAmount());
        assertEquals(100.0, debt.getPenaltyAmount());
    }

    @Test
    public void testMarkAsPaid() {
        Long debtId = 1L;
        Debt debt = new Debt();
        debt.setId(debtId);
        debt.setStatus(DebtStatus.ACTIVE);

        when(debtRepository.findById(debtId)).thenReturn(Optional.of(debt));

        debtService.markAsPaid(debtId);

        verify(debtRepository, times(1)).save(debt);
        assertEquals(DebtStatus.CLOSED, debt.getStatus());
    }
}
