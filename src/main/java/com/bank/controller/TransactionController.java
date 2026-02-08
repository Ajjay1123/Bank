package com.bank.controller;

import com.bank.dto.request.DepositRequest;
import com.bank.dto.request.FundTransferRequest;
import com.bank.dto.request.WithdrawalRequest;
import com.bank.dto.response.ApiResponse;
import com.bank.dto.response.PagedResponse;
import com.bank.dto.response.TransactionResponse;
import com.bank.security.UserPrincipal;
import com.bank.service.TransactionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/transactions")
@RequiredArgsConstructor
@Tag(name = "Transaction Management", description = "Transaction Operations (Deposit/Withdraw/Transfer)")
public class TransactionController {
    
    private final TransactionService transactionService;
    
    @PostMapping("/deposit")
    @Operation(summary = "Deposit money into an account")
    public ResponseEntity<ApiResponse<TransactionResponse>> deposit(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody DepositRequest request) {
        TransactionResponse response = transactionService.deposit(currentUser.getId(), request);
        return new ResponseEntity<>(
                ApiResponse.success("Deposit successful", response),
                HttpStatus.CREATED
        );
    }
    
    @PostMapping("/withdraw")
    @Operation(summary = "Withdraw money from an account")
    public ResponseEntity<ApiResponse<TransactionResponse>> withdraw(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody WithdrawalRequest request) {
        TransactionResponse response = transactionService.withdraw(currentUser.getId(), request);
        return new ResponseEntity<>(
                ApiResponse.success("Withdrawal successful", response),
                HttpStatus.CREATED
        );
    }
    
    @PostMapping("/transfer")
    @Operation(summary = "Transfer funds between accounts")
    public ResponseEntity<ApiResponse<TransactionResponse>> fundTransfer(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody FundTransferRequest request) {
        TransactionResponse response = transactionService.fundTransfer(currentUser.getId(), request);
        return new ResponseEntity<>(
                ApiResponse.success("Fund transfer successful", response),
                HttpStatus.CREATED
        );
    }
    
    @GetMapping("/statement")
    @Operation(summary = "Get account statement with pagination")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getAccountStatement(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam String accountNumber,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<TransactionResponse> response = transactionService.getAccountStatement(
                currentUser.getId(), accountNumber, page, size);
        return ResponseEntity.ok(ApiResponse.success("Account statement fetched successfully", response));
    }
    
    @GetMapping("/history")
    @Operation(summary = "Get transaction history with date range and pagination")
    public ResponseEntity<ApiResponse<PagedResponse<TransactionResponse>>> getTransactionHistory(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam String accountNumber,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<TransactionResponse> response = transactionService.getTransactionHistory(
                currentUser.getId(), accountNumber, startDate, endDate, page, size);
        return ResponseEntity.ok(ApiResponse.success("Transaction history fetched successfully", response));
    }
}
