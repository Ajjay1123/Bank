package com.bank.service;

import com.bank.dto.request.CustomerRegistrationRequest;
import com.bank.dto.request.LoginRequest;
import com.bank.dto.response.CustomerResponse;
import com.bank.dto.response.DashboardResponse;
import com.bank.dto.response.LoginResponse;
import com.bank.entity.Customer;
import com.bank.exception.BadRequestException;
import com.bank.repository.AccountRepository;
import com.bank.repository.CustomerRepository;
import com.bank.repository.TransactionRepository;
import com.bank.security.JwtTokenProvider;
import com.bank.security.UserPrincipal;
import com.bank.util.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
public class AuthService {
    
    private final CustomerRepository customerRepository;
    private final AccountRepository accountRepository;
    private final TransactionRepository transactionRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider tokenProvider;
    
    @Transactional
    public CustomerResponse registerCustomer(CustomerRegistrationRequest request) {
        // Check if username already exists
        if (customerRepository.existsByUsername(request.getUsername())) {
            throw new BadRequestException("Username is already taken");
        }
        
        // Check if email already exists
        if (customerRepository.existsByEmail(request.getEmail())) {
            throw new BadRequestException("Email is already registered");
        }
        
        // Create new customer
        Customer customer = Customer.builder()
                .firstName(request.getFirstName())
                .lastName(request.getLastName())
                .email(request.getEmail())
                .username(request.getUsername())
                .password(passwordEncoder.encode(request.getPassword()))
                .phone(request.getPhone())
                .address(request.getAddress())
                .status(Customer.CustomerStatus.ACTIVE)
                .build();
        
        Customer savedCustomer = customerRepository.save(customer);
        return ResponseMapper.toCustomerResponse(savedCustomer);
    }
    
    @Transactional
    public LoginResponse login(LoginRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );
        
        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);
        
        UserPrincipal userPrincipal = (UserPrincipal) authentication.getPrincipal();
        Customer customer = customerRepository.findById(userPrincipal.getId())
                .orElseThrow(() -> new BadRequestException("Customer not found"));
        
        return LoginResponse.builder()
                .token(jwt)
                .tokenType("Bearer")
                .customer(ResponseMapper.toCustomerResponse(customer))
                .build();
    }
    
    @Transactional(readOnly = true)
    public DashboardResponse getDashboard(Long customerId) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new BadRequestException("Customer not found"));
        
        int totalAccounts = accountRepository.findByCustomerId(customerId).size();
        
        BigDecimal totalBalance = accountRepository.findByCustomerId(customerId).stream()
                .map(account -> account.getBalance())
                .reduce(BigDecimal.ZERO, BigDecimal::add);
        
        int totalTransactions = (int) transactionRepository.findByCustomerId(customerId, 
                org.springframework.data.domain.Pageable.unpaged()).getTotalElements();
        
        return DashboardResponse.builder()
                .customer(ResponseMapper.toCustomerResponse(customer))
                .totalAccounts(totalAccounts)
                .totalBalance(totalBalance)
                .totalTransactions(totalTransactions)
                .build();
    }
}
