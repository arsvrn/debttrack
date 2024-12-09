package com.debttrack.platfrom.shelduler;

import com.debttrack.platfrom.enums.DebtStatus;
import com.debttrack.platfrom.enums.NotificationStatus;
import com.debttrack.platfrom.model.Debt;
import com.debttrack.platfrom.model.Notification;
import com.debttrack.platfrom.repository.DebtRepository;
import com.debttrack.platfrom.repository.NotificationRepository;
import com.debttrack.platfrom.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtScheduler {
    private final DebtRepository debtRepository;
    private final DebtService debtService;
    private final NotificationRepository notificationRepository;

    @Scheduled(cron = "0 0 8 * * ?") // Каждый день в 8 утра
    public void checkUpcomingDebts() {
        List<Debt> debts = debtRepository.findAll();
        for (Debt debt : debts) {
            if (debt.getStatus() == DebtStatus.ACTIVE &&
                    debt.getDueDate().isEqual(LocalDate.now().plusDays(3)) &&
                    debt.getBorrower().isNotificationsEnabled()) {
                createNotification(debt);
            }
        }
    }

    private void createNotification(Debt debt) {
        String message = String.format(
                "Напоминание: до возврата долга осталось 3 дня. Сумма: %.2f, Дата возврата: %s",
                debt.getAmount(), debt.getDueDate().toString()
        );

        Notification notification = new Notification();
        notification.setUser(debt.getBorrower());
        notification.setMessage(message);
        notification.setStatus(NotificationStatus.PENDING);

        notificationRepository.save(notification);
    }

    @Scheduled(cron = "0 0 0 * * ?") // Каждый день в полночь
    public void updateOverduePenalties() {
        List<Debt> activeDebts = debtRepository.findAll().stream()
                .filter(debt -> debt.getStatus() == DebtStatus.ACTIVE)
                .toList();

        for (Debt debt : activeDebts) {
            if (debt.getDueDate().isBefore(LocalDate.now())) {
                debtService.calculateDebtTotals(debt.getId());
            }
        }
    }
}