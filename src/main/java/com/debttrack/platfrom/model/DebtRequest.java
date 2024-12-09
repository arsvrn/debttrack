package com.debttrack.platfrom.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class DebtRequest {
    private Long creditorId;
    private Long borrowerId;
    private Double amount;
    private Double interestRate;
    private LocalDate dueDate;
    private String note;
    private Double penaltyAmount;
}