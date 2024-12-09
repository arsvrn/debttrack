package com.debttrack.platfrom.repository;

import com.debttrack.platfrom.enums.DebtStatus;
import com.debttrack.platfrom.model.Debt;
import com.debttrack.platfrom.model.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ActiveProfiles("test")
@DataJpaTest
public class DebtRepositoryTest {

    @Autowired
    private DebtRepository debtRepository;

    @Autowired
    private UserRepository userRepository;

    private User creditor;
    private User borrower;

    @BeforeEach
    public void setUp() {
        // Создание и сохранение кредитора
        creditor = new User();
        creditor.setEmail("creditor@example.com");
        creditor.setName("Creditor");
        creditor.setPasswordHash("hashed_password");
        creditor.setRole(com.debttrack.platfrom.enums.Role.USER);
        creditor = userRepository.save(creditor);

        // Создание и сохранение заемщика
        borrower = new User();
        borrower.setEmail("borrower@example.com");
        borrower.setName("Borrower");
        borrower.setPasswordHash("hashed_password");
        borrower.setRole(com.debttrack.platfrom.enums.Role.USER);
        borrower = userRepository.save(borrower);

        // Создание долгов
        Debt debt1 = new Debt();
        debt1.setCreditor(creditor);
        debt1.setBorrower(borrower);
        debt1.setAmount(100.0);
        debt1.setInterestRate(5.0);
        debt1.setDueDate(LocalDate.now().plusDays(30));
        debt1.setStatus(DebtStatus.ACTIVE);
        debt1.setTotalAmount(105.0);
        debt1.setPenaltyAmount(0.0);
        debtRepository.save(debt1);

        Debt debt2 = new Debt();
        debt2.setCreditor(creditor);
        debt2.setBorrower(borrower);
        debt2.setAmount(200.0);
        debt2.setInterestRate(10.0);
        debt2.setDueDate(LocalDate.now().plusDays(60));
        debt2.setStatus(DebtStatus.ACTIVE);
        debt2.setTotalAmount(220.0);
        debt2.setPenaltyAmount(0.0);
        debtRepository.save(debt2);

        Debt debt3 = new Debt();
        debt3.setCreditor(borrower);
        debt3.setBorrower(creditor);
        debt3.setAmount(300.0);
        debt3.setInterestRate(15.0);
        debt3.setDueDate(LocalDate.now().plusDays(90));
        debt3.setStatus(DebtStatus.ACTIVE);
        debt3.setTotalAmount(345.0);
        debt3.setPenaltyAmount(0.0);
        debtRepository.save(debt3);
    }

    @Test
    public void testFindByCreditor() {
        List<Debt> debts = debtRepository.findByCreditor(creditor);
        assertEquals(2, debts.size());
        for (Debt debt : debts) {
            assertEquals(creditor, debt.getCreditor());
        }
    }

    @Test
    public void testFindByBorrower() {
        List<Debt> debts = debtRepository.findByBorrower(borrower);
        assertEquals(2, debts.size());
        for (Debt debt : debts) {
            assertEquals(borrower, debt.getBorrower());
        }
    }

    @Test
    public void testFindByCreditor_NoDebts() {
        User newCreditor = new User();
        newCreditor.setEmail("newcreditor@example.com");
        newCreditor.setName("New Creditor");
        newCreditor.setPasswordHash("hashed_password");
        newCreditor.setRole(com.debttrack.platfrom.enums.Role.USER);
        newCreditor = userRepository.save(newCreditor);

        List<Debt> debts = debtRepository.findByCreditor(newCreditor);
        assertEquals(0, debts.size());
    }

    @Test
    public void testFindByBorrower_NoDebts() {
        User newBorrower = new User();
        newBorrower.setEmail("newborrower@example.com");
        newBorrower.setName("New Borrower");
        newBorrower.setPasswordHash("hashed_password");
        newBorrower.setRole(com.debttrack.platfrom.enums.Role.USER);
        newBorrower = userRepository.save(newBorrower);

        List<Debt> debts = debtRepository.findByBorrower(newBorrower);
        assertEquals(0, debts.size());
    }
}