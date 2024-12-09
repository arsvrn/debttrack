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
        try {
            debtSharingService.distributeDebt(groupId, totalAmount, interestRate, dueDate);
            return new ResponseEntity<>("Debt distributed evenly among group members", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input parameters", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to distribute debt", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PostMapping("/distribute-by-percentage")
    public ResponseEntity<?> distributeDebtByPercentage(@RequestParam Long groupId, @RequestBody Map<Long, Double> userPercentages,
                                                        @RequestParam Double totalAmount, @RequestParam Double interestRate,
                                                        @RequestParam LocalDate dueDate) {
        try {
            debtSharingService.distributeDebtByPercentage(groupId, userPercentages, totalAmount, interestRate, dueDate);
            return new ResponseEntity<>("Debt distributed by percentage among group members", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid input parameters", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to distribute debt by percentage", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
