package com.bank.service;

import com.bank.dto.request.AccountRequest;
import com.bank.dto.response.AccountResponse;
import com.bank.dto.response.PagedResponse;
import com.bank.entity.Account;
import com.bank.entity.Customer;
import com.bank.exception.BadRequestException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.AccountRepository;
import com.bank.repository.CustomerRepository;
import com.bank.util.AccountNumberGenerator;
import com.bank.util.ResponseMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class AccountService {
    
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    
    @Transactional
    public AccountResponse createAccount(Long customerId, AccountRequest request) {
        Customer customer = customerRepository.findById(customerId)
                .orElseThrow(() -> new ResourceNotFoundException("Customer not found with id: " + customerId));
        
        String accountNumber = AccountNumberGenerator.generateAccountNumber();
        
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountName(request.getAccountName())
                .accountType(request.getAccountType())
                .balance(BigDecimal.ZERO)
                .status(Account.AccountStatus.ACTIVE)
                .customer(customer)
                .build();
        
        Account savedAccount = accountRepository.save(account);
        return ResponseMapper.toAccountResponse(savedAccount);
    }
    
    @Transactional(readOnly = true)
    public AccountResponse getAccount(Long customerId, Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        
        if (!account.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You don't have access to this account");
        }
        
        return ResponseMapper.toAccountResponse(account);
    }
    
    @Transactional(readOnly = true)
    public List<AccountResponse> getAllAccounts(Long customerId) {
        return accountRepository.findByCustomerId(customerId).stream()
                .map(ResponseMapper::toAccountResponse)
                .collect(Collectors.toList());
    }
    
    @Transactional
    public AccountResponse updateAccount(Long customerId, Long accountId, AccountRequest request) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        
        if (!account.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You don't have access to this account");
        }
        
        account.setAccountName(request.getAccountName());
        account.setAccountType(request.getAccountType());
        
        Account updatedAccount = accountRepository.save(account);
        return ResponseMapper.toAccountResponse(updatedAccount);
    }
    
    @Transactional
    public void deleteAccount(Long customerId, Long accountId) {
        Account account = accountRepository.findById(accountId)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with id: " + accountId));
        
        if (!account.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You don't have access to this account");
        }
        
        if (account.getBalance().compareTo(BigDecimal.ZERO) > 0) {
            throw new BadRequestException("Cannot delete account with non-zero balance");
        }
        
        account.setStatus(Account.AccountStatus.CLOSED);
        accountRepository.save(account);
    }
    
    @Transactional(readOnly = true)
    public PagedResponse<AccountResponse> searchAccounts(
            Long customerId,
            String accountName,
            Account.AccountType accountType,
            Account.AccountStatus status,
            int page,
            int size) {
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        
        Page<Account> accountPage = accountRepository.searchAccounts(
                customerId, accountName, accountType, status, pageable);
        
        List<AccountResponse> content = accountPage.getContent().stream()
                .map(ResponseMapper::toAccountResponse)
                .collect(Collectors.toList());
        
        return PagedResponse.<AccountResponse>builder()
                .content(content)
                .pageNumber(accountPage.getNumber())
                .pageSize(accountPage.getSize())
                .totalElements(accountPage.getTotalElements())
                .totalPages(accountPage.getTotalPages())
                .last(accountPage.isLast())
                .first(accountPage.isFirst())
                .build();
    }
}
