package com.debttrack.platfrom.shelduler;

import com.debttrack.platfrom.enums.DebtStatus;
import com.debttrack.platfrom.model.Debt;
import com.debttrack.platfrom.model.Notification;
import com.debttrack.platfrom.model.User;
import com.debttrack.platfrom.repository.DebtRepository;
import com.debttrack.platfrom.repository.NotificationRepository;
import com.debttrack.platfrom.service.DebtService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;

import static org.mockito.Mockito.*;

public class DebtSchedulerTest {

    @Mock
    private DebtRepository debtRepository;

    @Mock
    private DebtService debtService;

    @Mock
    private NotificationRepository notificationRepository;

    @InjectMocks
    private DebtScheduler debtScheduler;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCheckUpcomingDebts() {
        User borrower = new User();
        borrower.setNotificationsEnabled(true);

        Debt debt1 = new Debt();
        debt1.setStatus(DebtStatus.ACTIVE);
        debt1.setDueDate(LocalDate.now().plusDays(3));
        debt1.setAmount(1000.0);
        debt1.setBorrower(borrower);

        Debt debt2 = new Debt();
        debt2.setStatus(DebtStatus.ACTIVE);
        debt2.setDueDate(LocalDate.now().plusDays(5));
        debt2.setAmount(2000.0);
        debt2.setBorrower(borrower);

        when(debtRepository.findAll()).thenReturn(Arrays.asList(debt1, debt2));

        debtScheduler.checkUpcomingDebts();

        verify(notificationRepository, times(1)).save(any(Notification.class));
    }

    @Test
    public void testUpdateOverduePenalties() {
        Debt debt1 = new Debt();
        debt1.setStatus(DebtStatus.ACTIVE);
        debt1.setDueDate(LocalDate.now().minusDays(1));
        debt1.setId(1L);

        Debt debt2 = new Debt();
        debt2.setStatus(DebtStatus.ACTIVE);
        debt2.setDueDate(LocalDate.now().plusDays(1));
        debt2.setId(2L);

        when(debtRepository.findAll()).thenReturn(Arrays.asList(debt1, debt2));

        debtScheduler.updateOverduePenalties();

        verify(debtService, times(1)).calculateDebtTotals(1L);
        verify(debtService, never()).calculateDebtTotals(2L);
    }
}
