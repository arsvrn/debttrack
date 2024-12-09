package com.debttrack.platfrom.model;

import com.debttrack.platfrom.enums.DebtStatus;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "debts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Debt {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "creditor_id", nullable = false)
    private User creditor;

    @ManyToOne
    @JoinColumn(name = "borrower_id", nullable = false)
    private User borrower;

    @Column(nullable = false)
    private Double amount; // Основная сумма

    @Column(nullable = false)
    private Double interestRate; // Процентная ставка

    @Column(nullable = false)
    private LocalDate dueDate;

    private String note;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private DebtStatus status = DebtStatus.ACTIVE;

    @Column(nullable = false)
    private Double totalAmount; // Итоговая сумма (с процентами)

    @Column(nullable = false)
    private Double penaltyAmount = 0.0; // Штраф за просрочку
}

