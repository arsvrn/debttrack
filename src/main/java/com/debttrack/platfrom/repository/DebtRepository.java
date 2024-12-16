package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.model.Debt;
import com.debttrack.platfrom.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface DebtRepository extends JpaRepository<Debt, Long> {
    List<Debt> findByCreditor(User creditor);
    List<Debt> findByBorrower(User borrower);
}