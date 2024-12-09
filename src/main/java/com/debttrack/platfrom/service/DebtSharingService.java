package com.debttrack.platfrom.service;

import com.debttrack.platfrom.enums.DebtStatus;
import com.debttrack.platfrom.model.Debt;
import com.debttrack.platfrom.model.User;
import com.debttrack.platfrom.repository.DebtRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class DebtSharingService {
    private final GroupService groupService;
    private final DebtRepository debtRepository;

    public void distributeDebt(Long groupId, Double totalAmount, Double interestRate, LocalDate dueDate) {
        List<User> members = groupService.getGroupMembers(groupId);
        if (members.isEmpty()) {
            throw new RuntimeException("Group has no members");
        }

        double sharePerMember = totalAmount / members.size();
        for (User member : members) {
            Debt debt = new Debt();
            debt.setCreditor(null); // Если коллективный долг, кредитор может быть null
            debt.setBorrower(member);
            debt.setAmount(sharePerMember);
            debt.setInterestRate(interestRate);
            debt.setDueDate(dueDate);
            debt.setStatus(DebtStatus.ACTIVE);
            debtRepository.save(debt);
        }
    }

    public void distributeDebtByPercentage(Long groupId, Map<Long, Double> userPercentages, Double totalAmount, Double interestRate, LocalDate dueDate) {
        List<User> members = groupService.getGroupMembers(groupId);

        for (User member : members) {
            Double percentage = userPercentages.getOrDefault(member.getId(), 0.0);
            double memberShare = totalAmount * (percentage / 100);

            Debt debt = new Debt();
            debt.setCreditor(null);
            debt.setBorrower(member);
            debt.setAmount(memberShare);
            debt.setInterestRate(interestRate);
            debt.setDueDate(dueDate);
            debt.setStatus(DebtStatus.ACTIVE);
            debtRepository.save(debt);
        }
    }
}