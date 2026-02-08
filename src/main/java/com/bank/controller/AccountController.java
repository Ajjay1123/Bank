package com.bank.controller;

import com.bank.dto.request.AccountRequest;
import com.bank.dto.response.AccountResponse;
import com.bank.dto.response.ApiResponse;
import com.bank.dto.response.PagedResponse;
import com.bank.entity.Account;
import com.bank.security.UserPrincipal;
import com.bank.service.AccountService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/accounts")
@RequiredArgsConstructor
@Tag(name = "Account Management", description = "Bank Account CRUD Operations")
public class AccountController {
    
    private final AccountService accountService;
    
    @PostMapping
    @Operation(summary = "Create a new bank account")
    public ResponseEntity<ApiResponse<AccountResponse>> createAccount(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.createAccount(currentUser.getId(), request);
        return new ResponseEntity<>(
                ApiResponse.success("Account created successfully", response),
                HttpStatus.CREATED
        );
    }
    
    @GetMapping("/{accountId}")
    @Operation(summary = "Get account by ID")
    public ResponseEntity<ApiResponse<AccountResponse>> getAccount(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long accountId) {
        AccountResponse response = accountService.getAccount(currentUser.getId(), accountId);
        return ResponseEntity.ok(ApiResponse.success("Account fetched successfully", response));
    }
    
    @GetMapping
    @Operation(summary = "Get all accounts for logged-in customer")
    public ResponseEntity<ApiResponse<List<AccountResponse>>> getAllAccounts(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        List<AccountResponse> response = accountService.getAllAccounts(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Accounts fetched successfully", response));
    }
    
    @PutMapping("/{accountId}")
    @Operation(summary = "Update account details")
    public ResponseEntity<ApiResponse<AccountResponse>> updateAccount(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long accountId,
            @Valid @RequestBody AccountRequest request) {
        AccountResponse response = accountService.updateAccount(currentUser.getId(), accountId, request);
        return ResponseEntity.ok(ApiResponse.success("Account updated successfully", response));
    }
    
    @DeleteMapping("/{accountId}")
    @Operation(summary = "Delete/Close an account")
    public ResponseEntity<ApiResponse<String>> deleteAccount(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @PathVariable Long accountId) {
        accountService.deleteAccount(currentUser.getId(), accountId);
        return ResponseEntity.ok(ApiResponse.success("Account closed successfully", "Account has been closed"));
    }
    
    @GetMapping("/search")
    @Operation(summary = "Search and filter accounts")
    public ResponseEntity<ApiResponse<PagedResponse<AccountResponse>>> searchAccounts(
            @AuthenticationPrincipal UserPrincipal currentUser,
            @RequestParam(required = false) String accountName,
            @RequestParam(required = false) Account.AccountType accountType,
            @RequestParam(required = false) Account.AccountStatus status,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        PagedResponse<AccountResponse> response = accountService.searchAccounts(
                currentUser.getId(), accountName, accountType, status, page, size);
        return ResponseEntity.ok(ApiResponse.success("Accounts search completed", response));
    }
}
