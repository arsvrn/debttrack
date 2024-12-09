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
        try {
            Debt debt = debtService.addDebt(request);
            return new ResponseEntity<>(debt, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid debt request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to add debt: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PutMapping("/{debtId}")
    public ResponseEntity<?> updateDebt(@PathVariable Long debtId, @RequestBody DebtRequest request) {
        try {
            Debt debt = debtService.updateDebt(debtId, request);
            return new ResponseEntity<>(debt, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Invalid debt request: " + e.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to update debt: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @DeleteMapping("/{debtId}")
    public ResponseEntity<?> deleteDebt(@PathVariable Long debtId) {
        try {
            debtService.deleteDebt(debtId);
            return new ResponseEntity<>("Debt deleted successfully", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Debt not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to delete debt: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/creditor/{creditorId}")
    public ResponseEntity<?> getDebtsForCreditor(@PathVariable Long creditorId) {
        try {
            List<Debt> debts = debtService.getDebtsForCreditor(creditorId);
            return new ResponseEntity<>(debts, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Creditor not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve debts for creditor: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @GetMapping("/borrower/{borrowerId}")
    public ResponseEntity<?> getDebtsForBorrower(@PathVariable Long borrowerId) {
        try {
            List<Debt> debts = debtService.getDebtsForBorrower(borrowerId);
            return new ResponseEntity<>(debts, HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Borrower not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to retrieve debts for borrower: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{debtId}/calculate")
    public ResponseEntity<?> calculateDebt(@PathVariable Long debtId) {
        try {
            debtService.calculateDebtTotals(debtId);
            return new ResponseEntity<>("Debt totals calculated", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Debt not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to calculate debt totals: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    @PatchMapping("/{debtId}/close")
    public ResponseEntity<?> closeDebt(@PathVariable Long debtId) {
        try {
            debtService.markAsPaid(debtId);
            return new ResponseEntity<>("Debt marked as paid", HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>("Debt not found: " + e.getMessage(), HttpStatus.NOT_FOUND);
        } catch (Exception e) {
            return new ResponseEntity<>("Failed to mark debt as paid: " + e.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
