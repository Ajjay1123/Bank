package com.bank.controller;

import com.bank.dto.request.CustomerRegistrationRequest;
import com.bank.dto.request.LoginRequest;
import com.bank.dto.response.ApiResponse;
import com.bank.dto.response.CustomerResponse;
import com.bank.dto.response.DashboardResponse;
import com.bank.dto.response.LoginResponse;
import com.bank.security.UserPrincipal;
import com.bank.service.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Authentication and Authorization APIs")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new customer")
    public ResponseEntity<ApiResponse<CustomerResponse>> register(
            @Valid @RequestBody CustomerRegistrationRequest request) {
        CustomerResponse response = authService.registerCustomer(request);
        return new ResponseEntity<>(
                ApiResponse.success("Customer registered successfully", response),
                ApiResponse.success("Customer registered successfully", authService),
                HttpStatus.CREATED);
    }

    @PostMapping("/login")
    @Operation(summary = "Login and get JWT token")
    public ResponseEntity<ApiResponse<LoginResponse>> login(
            @Valid @RequestBody LoginRequest request) {
        LoginResponse response = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", response));
    }

    @GetMapping("/dashboard")
    @Operation(summary = "Get customer dashboard with summary")
    public ResponseEntity<ApiResponse<DashboardResponse>> getDashboard(
            @AuthenticationPrincipal UserPrincipal currentUser) {
        DashboardResponse response = authService.getDashboard(currentUser.getId());
        return ResponseEntity.ok(ApiResponse.success("Dashboard fetched successfully", response));
    }

    @PostMapping("/logout")
    @Operation(summary = "Logout (client should discard token)")
    public ResponseEntity<ApiResponse<String>> logout() {
        return ResponseEntity.ok(ApiResponse.success("Logout successful", "Token invalidated"));
    }
}
