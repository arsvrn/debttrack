package com.debttrack.platfrom.shelduler;

import com.debttrack.platfrom.enums.DebtStatus;
import com.debttrack.platfrom.enums.NotificationStatus;
import com.debttrack.platfrom.model.Debt;
import com.debttrack.platfrom.model.Notification;
import com.debttrack.platfrom.repository.DebtRepository;
import com.debttrack.platfrom.repository.NotificationRepository;
import com.debttrack.platfrom.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtScheduler {
    private final DebtService debtService;
    private final NotificationRepository notificationRepository;
    @Scheduled(cron = "${scheduling.cron.checkUpcomingDebts}")
    public void checkUpcomingDebts() {
        List<Debt> debts = debtService.findAll();
        for (Debt debt : debts) {
            if (checkDebts(debt)) {
                createNotification(debt);
            }
        }
    }

    private boolean checkDebts(Debt debt) {
        return debt.getStatus() == DebtStatus.ACTIVE &&
                debt.getDueDate().isEqual(LocalDate.now().plusDays(3)) &&
                debt.getBorrower().isNotificationsEnabled();
    }

    private void createNotification(Debt debt) {
        String message = String.format(
                "Напоминание: до возврата долга осталось 3 дня. Сумма: %.2f, Дата возврата: %s",
                debt.getAmount(), debt.getDueDate().toString()
        );

        Notification notification = Notification.builder()
                .user(debt.getBorrower())
                .message(message)
                .status(NotificationStatus.PENDING)
                .build();

        notificationRepository.save(notification);
    }

    @Scheduled(cron = "${scheduling.cron.updateOverduePenalties}")
    public void updateOverduePenalties() {
        debtService.findAll().stream()
                .filter(debt -> debt.getStatus() == DebtStatus.ACTIVE)
                .filter(debt -> debt.getDueDate().isBefore(LocalDate.now()))
                .forEach(debt -> debtService.calculateDebtTotals(debt.getId()));
    }
}