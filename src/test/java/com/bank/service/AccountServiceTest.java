package com.bank.service;

import com.bank.dto.request.AccountRequest;
import com.bank.dto.response.AccountResponse;
import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.AccountRepository;
import com.bank.repository.CustomerRepository;
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
class AccountServiceTest {
    
    @Mock
    private AccountRepository accountRepository;
    
    @Mock
    private CustomerRepository customerRepository;
    
    @InjectMocks
    private AccountService accountService;
    
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
                .balance(BigDecimal.ZERO)
                .status(Account.AccountStatus.ACTIVE)
                .customer(customer)
                .build();
    }
    
    @Test
    void createAccount_Success() {
        AccountRequest request = AccountRequest.builder()
                .accountName("Savings Account")
                .accountType(Account.AccountType.SAVINGS)
                .build();
        
        when(customerRepository.findById(1L)).thenReturn(Optional.of(customer));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        
        AccountResponse response = accountService.createAccount(1L, request);
        
        assertNotNull(response);
        assertEquals("Test Account", response.getAccountName());
        assertEquals(Account.AccountType.SAVINGS, response.getAccountType());
        verify(accountRepository, times(1)).save(any(Account.class));
    }
    
    @Test
    void createAccount_CustomerNotFound() {
        AccountRequest request = AccountRequest.builder()
                .accountName("Savings Account")
                .accountType(Account.AccountType.SAVINGS)
                .build();
        
        when(customerRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.createAccount(999L, request);
        });
    }
    
    @Test
    void getAccount_Success() {
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        
        AccountResponse response = accountService.getAccount(1L, 1L);
        
        assertNotNull(response);
        assertEquals("ACC123456", response.getAccountNumber());
    }
    
    @Test
    void getAccount_NotFound() {
        when(accountRepository.findById(999L)).thenReturn(Optional.empty());
        
        assertThrows(ResourceNotFoundException.class, () -> {
            accountService.getAccount(1L, 999L);
        });
    }
    
    @Test
    void updateAccount_Success() {
        AccountRequest request = AccountRequest.builder()
                .accountName("Updated Account")
                .accountType(Account.AccountType.CURRENT)
                .build();
        
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.save(any(Account.class))).thenReturn(account);
        
        AccountResponse response = accountService.updateAccount(1L, 1L, request);
        
        assertNotNull(response);
        verify(accountRepository, times(1)).save(any(Account.class));
    }
}
