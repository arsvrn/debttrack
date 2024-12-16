package com.debttrack.platfrom.controller;

import com.debttrack.platfrom.model.Debt;
import com.debttrack.platfrom.model.DebtRequest;
import com.debttrack.platfrom.service.DebtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/debts")
@RequiredArgsConstructor
public class DebtController {
    private final DebtService debtService;

    @PostMapping
    public ResponseEntity<?> addDebt(@RequestBody DebtRequest request) {
        Debt debt = debtService.addDebt(request);
        return new ResponseEntity<>(debt, HttpStatus.CREATED);
    }

    @PutMapping("/{debtId}")
    public ResponseEntity<?> updateDebt(@PathVariable Long debtId, @RequestBody DebtRequest request) {
        Debt debt = debtService.updateDebt(debtId, request);
        return new ResponseEntity<>(debt, HttpStatus.OK);
    }

    @DeleteMapping("/{debtId}")
    public ResponseEntity<?> deleteDebt(@PathVariable Long debtId) {
        debtService.deleteDebt(debtId);
        return new ResponseEntity<>("Debt deleted successfully", HttpStatus.OK);
    }

    @GetMapping("/creditor/{creditorId}")
    public ResponseEntity<?> getDebtsForCreditor(@PathVariable Long creditorId) {
        List<Debt> debts = debtService.getDebtsForCreditor(creditorId);
        return new ResponseEntity<>(debts, HttpStatus.OK);
    }

    @GetMapping("/borrower/{borrowerId}")
    public ResponseEntity<?> getDebtsForBorrower(@PathVariable Long borrowerId) {
        List<Debt> debts = debtService.getDebtsForBorrower(borrowerId);
        return new ResponseEntity<>(debts, HttpStatus.OK);
    }

    @PatchMapping("/{debtId}/calculate")
    public ResponseEntity<?> calculateDebt(@PathVariable Long debtId) {
        debtService.calculateDebtTotals(debtId);
        return new ResponseEntity<>("Debt totals calculated", HttpStatus.OK);
    }

    @PatchMapping("/{debtId}/close")
    public ResponseEntity<?> closeDebt(@PathVariable Long debtId) {
        debtService.markAsPaid(debtId);
        return new ResponseEntity<>("Debt marked as paid", HttpStatus.OK);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegalArgumentException(IllegalArgumentException e) {
        return new ResponseEntity<>(e.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<String> handleException(Exception e) {
        return new ResponseEntity<>("An error occurred: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
