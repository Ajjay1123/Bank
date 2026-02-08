package com.bank.service;

import com.bank.dto.request.DepositRequest;
import com.bank.dto.request.FundTransferRequest;
import com.bank.dto.request.WithdrawalRequest;
import com.bank.dto.response.PagedResponse;
import com.bank.dto.response.TransactionResponse;
import com.bank.entity.Account;
import com.bank.entity.Transaction;
import com.bank.exception.AccountInactiveException;
import com.bank.exception.BadRequestException;
import com.bank.exception.InsufficientBalanceException;
import com.bank.exception.ResourceNotFoundException;
import com.bank.repository.AccountRepository;
import com.bank.repository.TransactionRepository;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class TransactionService {
    
    private final TransactionRepository transactionRepository;
    private final AccountRepository accountRepository;
    
    @Transactional
    public TransactionResponse deposit(Long customerId, DepositRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + request.getAccountNumber()));
        
        if (!account.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You don't have access to this account");
        }
        
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new AccountInactiveException("Account is not active");
        }
        
        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfter = balanceBefore.add(request.getAmount());
        
        account.setBalance(balanceAfter);
        accountRepository.save(account);
        
        Transaction transaction = Transaction.builder()
                .transactionId(AccountNumberGenerator.generateTransactionId())
                .type(Transaction.TransactionType.DEPOSIT)
                .amount(request.getAmount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description(request.getDescription())
                .status(Transaction.TransactionStatus.SUCCESS)
                .account(account)
                .build();
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        return ResponseMapper.toTransactionResponse(savedTransaction);
    }
    
    @Transactional
    public TransactionResponse withdraw(Long customerId, WithdrawalRequest request) {
        Account account = accountRepository.findByAccountNumber(request.getAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + request.getAccountNumber()));
        
        if (!account.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You don't have access to this account");
        }
        
        if (account.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new AccountInactiveException("Account is not active");
        }
        
        if (account.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for withdrawal");
        }
        
        BigDecimal balanceBefore = account.getBalance();
        BigDecimal balanceAfter = balanceBefore.subtract(request.getAmount());
        
        account.setBalance(balanceAfter);
        accountRepository.save(account);
        
        Transaction transaction = Transaction.builder()
                .transactionId(AccountNumberGenerator.generateTransactionId())
                .type(Transaction.TransactionType.WITHDRAWAL)
                .amount(request.getAmount())
                .balanceBefore(balanceBefore)
                .balanceAfter(balanceAfter)
                .description(request.getDescription())
                .status(Transaction.TransactionStatus.SUCCESS)
                .account(account)
                .build();
        
        Transaction savedTransaction = transactionRepository.save(transaction);
        return ResponseMapper.toTransactionResponse(savedTransaction);
    }
    
    @Transactional
    public TransactionResponse fundTransfer(Long customerId, FundTransferRequest request) {
        if (request.getFromAccountNumber().equals(request.getToAccountNumber())) {
            throw new BadRequestException("Cannot transfer to the same account");
        }
        
        Account fromAccount = accountRepository.findByAccountNumber(request.getFromAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Source account not found"));
        
        Account toAccount = accountRepository.findByAccountNumber(request.getToAccountNumber())
                .orElseThrow(() -> new ResourceNotFoundException("Destination account not found"));
        
        if (!fromAccount.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You don't have access to the source account");
        }
        
        if (fromAccount.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new AccountInactiveException("Source account is not active");
        }
        
        if (toAccount.getStatus() != Account.AccountStatus.ACTIVE) {
            throw new AccountInactiveException("Destination account is not active");
        }
        
        if (fromAccount.getBalance().compareTo(request.getAmount()) < 0) {
            throw new InsufficientBalanceException("Insufficient balance for transfer");
        }
        
        // Debit from source account
        BigDecimal fromBalanceBefore = fromAccount.getBalance();
        BigDecimal fromBalanceAfter = fromBalanceBefore.subtract(request.getAmount());
        fromAccount.setBalance(fromBalanceAfter);
        accountRepository.save(fromAccount);
        
        // Credit to destination account
        BigDecimal toBalanceBefore = toAccount.getBalance();
        BigDecimal toBalanceAfter = toBalanceBefore.add(request.getAmount());
        toAccount.setBalance(toBalanceAfter);
        accountRepository.save(toAccount);
        
        // Create debit transaction
        Transaction debitTransaction = Transaction.builder()
                .transactionId(AccountNumberGenerator.generateTransactionId())
                .type(Transaction.TransactionType.TRANSFER_OUT)
                .amount(request.getAmount())
                .balanceBefore(fromBalanceBefore)
                .balanceAfter(fromBalanceAfter)
                .description(request.getDescription())
                .status(Transaction.TransactionStatus.SUCCESS)
                .account(fromAccount)
                .fromAccountNumber(request.getFromAccountNumber())
                .toAccountNumber(request.getToAccountNumber())
                .build();
        transactionRepository.save(debitTransaction);
        
        // Create credit transaction
        Transaction creditTransaction = Transaction.builder()
                .transactionId(AccountNumberGenerator.generateTransactionId())
                .type(Transaction.TransactionType.TRANSFER_IN)
                .amount(request.getAmount())
                .balanceBefore(toBalanceBefore)
                .balanceAfter(toBalanceAfter)
                .description(request.getDescription())
                .status(Transaction.TransactionStatus.SUCCESS)
                .account(toAccount)
                .fromAccountNumber(request.getFromAccountNumber())
                .toAccountNumber(request.getToAccountNumber())
                .build();
        transactionRepository.save(creditTransaction);
        
        return ResponseMapper.toTransactionResponse(debitTransaction);
    }
    
    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getAccountStatement(
            Long customerId,
            String accountNumber,
            int page,
            int size) {
        
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
        
        if (!account.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You don't have access to this account");
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Transaction> transactionPage = transactionRepository.findByAccountId(account.getId(), pageable);
        
        List<TransactionResponse> content = transactionPage.getContent().stream()
                .map(ResponseMapper::toTransactionResponse)
                .collect(Collectors.toList());
        
        return PagedResponse.<TransactionResponse>builder()
                .content(content)
                .pageNumber(transactionPage.getNumber())
                .pageSize(transactionPage.getSize())
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .last(transactionPage.isLast())
                .first(transactionPage.isFirst())
                .build();
    }
    
    @Transactional(readOnly = true)
    public PagedResponse<TransactionResponse> getTransactionHistory(
            Long customerId,
            String accountNumber,
            LocalDateTime startDate,
            LocalDateTime endDate,
            int page,
            int size) {
        
        Account account = accountRepository.findByAccountNumber(accountNumber)
                .orElseThrow(() -> new ResourceNotFoundException("Account not found with number: " + accountNumber));
        
        if (!account.getCustomer().getId().equals(customerId)) {
            throw new BadRequestException("You don't have access to this account");
        }
        
        Pageable pageable = PageRequest.of(page, size, Sort.by("createdAt").descending());
        Page<Transaction> transactionPage = transactionRepository.findByAccountIdAndCreatedAtBetween(
                account.getId(), startDate, endDate, pageable);
        
        List<TransactionResponse> content = transactionPage.getContent().stream()
                .map(ResponseMapper::toTransactionResponse)
                .collect(Collectors.toList());
        
        return PagedResponse.<TransactionResponse>builder()
                .content(content)
                .pageNumber(transactionPage.getNumber())
                .pageSize(transactionPage.getSize())
                .totalElements(transactionPage.getTotalElements())
                .totalPages(transactionPage.getTotalPages())
                .last(transactionPage.isLast())
                .first(transactionPage.isFirst())
                .build();
    }
}
