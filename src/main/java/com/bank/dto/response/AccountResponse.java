package com.bank.dto.response;

import com.bank.entity.Account;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountResponse {
    private Long id;
    private String accountNumber;
    private String accountName;
    private Account.AccountType accountType;
    private BigDecimal balance;
    private Account.AccountStatus status;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
