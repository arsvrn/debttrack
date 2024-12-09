package com.debttrack.platfrom.service;

import com.debttrack.platfrom.enums.DebtStatus;
import com.debttrack.platfrom.model.Debt;
import com.debttrack.platfrom.model.User;
import com.debttrack.platfrom.repository.DebtRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class DebtSharingServiceTest {

    @Mock
    private GroupService groupService;

    @Mock
    private DebtRepository debtRepository;

    @InjectMocks
    private DebtSharingService debtSharingService;

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

        User member1 = new User();
        member1.setId(1L);
        User member2 = new User();
        member2.setId(2L);

        when(groupService.getGroupMembers(groupId)).thenReturn(Arrays.asList(member1, member2));

        debtSharingService.distributeDebt(groupId, totalAmount, interestRate, dueDate);

        verify(debtRepository, times(2)).save(any(Debt.class));
    }

    @Test
    public void testDistributeDebt_NoMembers() {
        Long groupId = 1L;
        Double totalAmount = 1000.0;
        Double interestRate = 5.0;
        LocalDate dueDate = LocalDate.now().plusDays(30);

        when(groupService.getGroupMembers(groupId)).thenReturn(List.of());

        assertThrows(RuntimeException.class, () -> {
            debtSharingService.distributeDebt(groupId, totalAmount, interestRate, dueDate);
        });
    }

    @Test
    public void testDistributeDebtByPercentage_Success() {
        Long groupId = 1L;
        Double totalAmount = 1000.0;
        Double interestRate = 5.0;
        LocalDate dueDate = LocalDate.now().plusDays(30);

        User member1 = new User();
        member1.setId(1L);
        User member2 = new User();
        member2.setId(2L);

        Map<Long, Double> userPercentages = new HashMap<>();
        userPercentages.put(1L, 60.0);
        userPercentages.put(2L, 40.0);

        when(groupService.getGroupMembers(groupId)).thenReturn(Arrays.asList(member1, member2));

        debtSharingService.distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);

        verify(debtRepository, times(2)).save(any(Debt.class));
    }

    @Test
    public void testDistributeDebtByPercentage_NoPercentage() {
        Long groupId = 1L;
        Double totalAmount = 1000.0;
        Double interestRate = 5.0;
        LocalDate dueDate = LocalDate.now().plusDays(30);

        User member1 = new User();
        member1.setId(1L);
        User member2 = new User();
        member2.setId(2L);

        Map<Long, Double> userPercentages = new HashMap<>();
        userPercentages.put(1L, 60.0);

        when(groupService.getGroupMembers(groupId)).thenReturn(Arrays.asList(member1, member2));

        debtSharingService.distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);

        verify(debtRepository, times(2)).save(any(Debt.class));
    }
}
