package com.bank.dto.response;

import com.bank.entity.Transaction;
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
public class TransactionResponse {
    private Long id;
    private String transactionId;
    private Transaction.TransactionType type;
    private BigDecimal amount;
    private BigDecimal balanceBefore;
    private BigDecimal balanceAfter;
    private String description;
    private Transaction.TransactionStatus status;
    private String accountNumber;
    private String fromAccountNumber;
    private String toAccountNumber;
    private LocalDateTime createdAt;
}
