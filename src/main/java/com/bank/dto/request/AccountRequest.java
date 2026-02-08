package com.bank.dto.request;

import com.bank.entity.Account;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AccountRequest {
    
    @NotBlank(message = "Account name is required")
    @Size(min = 3, max = 100, message = "Account name must be between 3 and 100 characters")
    private String accountName;
    
    @NotNull(message = "Account type is required")
    private Account.AccountType accountType;
}
