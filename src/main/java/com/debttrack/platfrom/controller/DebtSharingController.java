package com.debttrack.platfrom.controller;

import com.debttrack.platfrom.service.DebtSharingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.Map;

@RestController
@RequestMapping("/debts")
@RequiredArgsConstructor
public class DebtSharingController {
    private final DebtSharingService debtSharingService;

    @PostMapping("/distribute")
    public ResponseEntity<?> distributeDebt(@RequestParam Long groupId, @RequestParam Double totalAmount,
                                            @RequestParam Double interestRate, @RequestParam LocalDate dueDate) {
        debtSharingService.distributeDebt(groupId, totalAmount, interestRate, dueDate);
        return new ResponseEntity<>("Debt distributed evenly among group members", HttpStatus.OK);
    }

    @PostMapping("/distribute-by-percentage")
    public ResponseEntity<?> distributeDebtByPercentage(@RequestParam Long groupId, @RequestBody Map<Long, Double> userPercentages,
                                                        @RequestParam Double totalAmount, @RequestParam Double interestRate,
                                                        @RequestParam LocalDate dueDate) {
        debtSharingService.distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);
        return new ResponseEntity<>("Debt distributed by percentage among group members", HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>("Invalid input parameters: " + e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
