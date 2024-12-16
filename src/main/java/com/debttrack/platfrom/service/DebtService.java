package com.debttrack.platfrom.service;

import com.debttrack.platfrom.enums.DebtStatus;
import com.debttrack.platfrom.model.Debt;
import com.debttrack.platfrom.model.DebtRequest;
import com.debttrack.platfrom.model.User;
import com.debttrack.platfrom.repository.DebtRepository;
import com.debttrack.platfrom.repository.UserRepository;
import com.debttrack.platfrom.utils.DebtCalculator;
import com.debttrack.platfrom.utils.ValidationUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
@RequiredArgsConstructor
public class DebtService {
    private final DebtRepository debtRepository;
    private final UserRepository userRepository;
    private final DebtCalculator debtCalculator;

    public Debt addDebt(DebtRequest request) {
        ValidationUtils.validateDebtRequest(request);

        User creditor = userRepository.findById(request.getCreditorId())
                .orElseThrow(() -> new RuntimeException("Creditor not found"));
        User borrower = userRepository.findById(request.getBorrowerId())
                .orElseThrow(() -> new RuntimeException("Borrower not found"));

        Debt debt = Debt.builder()
                .creditor(creditor)
                .borrower(borrower)
                .amount(request.getAmount())
                .interestRate(request.getInterestRate())
                .dueDate(request.getDueDate())
                .note(request.getNote())
                .status(DebtStatus.ACTIVE)
                .penaltyAmount(request.getPenaltyAmount())
                .build();
        calcTotals(debt);
        return debtRepository.save(debt);
    }

    public Debt updateDebt(Long debtId, DebtRequest request) {
        ValidationUtils.validateDebtRequest(request);

        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));

        debt.setAmount(request.getAmount());
        debt.setInterestRate(request.getInterestRate());
        debt.setDueDate(request.getDueDate());
        debt.setNote(request.getNote());

        return debtRepository.save(debt);
    }

    public void deleteDebt(Long debtId) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));
        debtRepository.delete(debt);
    }

    public List<Debt> getDebtsForCreditor(Long creditorId) {
        User creditor = userRepository.findById(creditorId)
                .orElseThrow(() -> new RuntimeException("Creditor not found"));
        return debtRepository.findByCreditor(creditor);
    }

    public List<Debt> getDebtsForBorrower(Long borrowerId) {
        User borrower = userRepository.findById(borrowerId)
                .orElseThrow(() -> new RuntimeException("Borrower not found"));
        return debtRepository.findByBorrower(borrower);
    }

    public void calculateDebtTotals(Long debtId) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));
        calcTotals(debt);
        debtRepository.save(debt);
    }

    private void calcTotals(Debt debt) {
        Double penaltyRatePerDay = debt.getPenaltyAmount();

        debt.setTotalAmount(debtCalculator.calculateTotalAmount(debt.getAmount(), debt.getInterestRate()));

        if (debt.getStatus() == DebtStatus.ACTIVE && debt.getDueDate().isBefore(LocalDate.now())) {
            long overdueDays = ChronoUnit.DAYS.between(debt.getDueDate(), LocalDate.now());
            debt.setPenaltyAmount(debtCalculator.calculatePenalty(debt.getAmount(), overdueDays, penaltyRatePerDay));
        }
    }

    public void markAsPaid(Long debtId) {
        Debt debt = debtRepository.findById(debtId)
                .orElseThrow(() -> new RuntimeException("Debt not found"));
        debt.setStatus(DebtStatus.CLOSED);
        debtRepository.save(debt);
    }

    public List<Debt> findAll() {
        return debtRepository.findAll();
    }
}