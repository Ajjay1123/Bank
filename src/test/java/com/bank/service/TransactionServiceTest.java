package com.bank.service;

import com.bank.dto.request.DepositRequest;
import com.bank.dto.request.WithdrawalRequest;
import com.bank.dto.response.TransactionResponse;
import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.entity.Transaction;
import com.bank.exception.AccountInactiveException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class TransactionServiceTest {
    
    @Mock
    private TransactionRepository transactionRepository;
    
    @Mock
    private AccountRepository accountRepository;
    
    @InjectMocks
    private TransactionService transactionService;
    
    private Customer customer;
    private Account account;
    
    @BeforeEach
    void setUp() {
        customer = Customer.builder()
                .id(1L)
                .username("testuser")
                .firstName("Test")
                .lastName("User")
                .email("test@example.com")
                .status(Customer.CustomerStatus.ACTIVE)
                .build();
        
        account = Account.builder()
                .id(1L)
                .accountNumber("ACC123456")
                .accountName("Test Account")
                .accountType(Account.AccountType.SAVINGS)
                .balance(new BigDecimal("1000.00"))
                .status(Account.AccountStatus.ACTIVE)
                .customer(customer)
                .build();
    }
    
    @Test
    void deposit_Success() {
        DepositRequest request = DepositRequest.builder()
                .accountNumber("ACC123456")
                .amount(new BigDecimal("500.00"))
                .description("Test deposit")
                .build();
        
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TXN123")
                .type(Transaction.TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .balanceBefore(new BigDecimal("1000.00"))
                .balanceAfter(new BigDecimal("1500.00"))
                .status(Transaction.TransactionStatus.SUCCESS)
                .account(account)
                .build();
        
        when(accountRepository.findByAccountNumber("ACC123456")).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        
        TransactionResponse response = transactionService.deposit(1L, request);
        
        assertNotNull(response);
        assertEquals(Transaction.TransactionType.DEPOSIT, response.getType());
        assertEquals(new BigDecimal("500.00"), response.getAmount());
        verify(accountRepository, times(1)).save(any(Account.class));
        verify(transactionRepository, times(1)).save(any(Transaction.class));
    }
    
    @Test
    void deposit_AccountNotFound() {
        DepositRequest request = DepositRequest.builder()
                .accountNumber("INVALID")
                .amount(new BigDecimal("500.00"))
                .build();
        
        when(accountRepository.findByAccountNumber("INVALID")).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            transactionService.deposit(1L, request);
        });
    }
    
    @Test
    void withdraw_Success() {
        WithdrawalRequest request = WithdrawalRequest.builder()
                .accountNumber("ACC123456")
                .amount(new BigDecimal("300.00"))
                .description("Test withdrawal")
                .build();
        
        Transaction transaction = Transaction.builder()
                .id(1L)
                .transactionId("TXN124")
                .type(Transaction.TransactionType.WITHDRAWAL)
                .amount(request.getAmount())
                .balanceBefore(new BigDecimal("1000.00"))
                .balanceAfter(new BigDecimal("700.00"))
                .status(Transaction.TransactionStatus.SUCCESS)
                .account(account)
                .build();
        
        when(accountRepository.findByAccountNumber("ACC123456")).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        when(transactionRepository.save(any(Transaction.class))).thenReturn(transaction);
        
        TransactionResponse response = transactionService.withdraw(1L, request);
        
        assertNotNull(response);
        assertEquals(Transaction.TransactionType.WITHDRAWAL, response.getType());
        assertEquals(new BigDecimal("300.00"), response.getAmount());
    }
    
    @Test
    void withdraw_InsufficientBalance() {
        WithdrawalRequest request = WithdrawalRequest.builder()
                .accountNumber("ACC123456")
                .amount(new BigDecimal("2000.00"))
                .build();
        
        when(accountRepository.findByAccountNumber("ACC123456")).thenReturn(Optional.of(account));
        
        assertThrows(InsufficientBalanceException.class, () -> {
            transactionService.withdraw(1L, request);
        });
    }
    
    @Test
    void withdraw_InactiveAccount() {
        account.setStatus(Account.AccountStatus.INACTIVE);
        
        WithdrawalRequest request = WithdrawalRequest.builder()
                .accountNumber("ACC123456")
                .amount(new BigDecimal("100.00"))
                .build();
        
        when(accountRepository.findByAccountNumber("ACC123456")).thenReturn(Optional.of(account));
        
        assertThrows(AccountInactiveException.class, () -> {
            transactionService.withdraw(1L, request);
        });
    }
}
