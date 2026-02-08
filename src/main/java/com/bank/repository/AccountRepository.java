package com.bank.repository;

import com.bank.entity.Account;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AccountRepository extends JpaRepository<Account, Long> {
    Optional<Account> findByAccountNumber(String accountNumber);
    List<Account> findByCustomerId(Long customerId);
    boolean existsByAccountNumber(String accountNumber);
    
    @Query("SELECT a FROM Account a WHERE a.customer.id = :customerId " +
           "AND (:accountName IS NULL OR LOWER(a.accountName) LIKE LOWER(CONCAT('%', :accountName, '%'))) " +
           "AND (:accountType IS NULL OR a.accountType = :accountType) " +
           "AND (:status IS NULL OR a.status = :status)")
    Page<Account> searchAccounts(
            @Param("customerId") Long customerId,
            @Param("accountName") String accountName,
            @Param("accountType") Account.AccountType accountType,
            @Param("status") Account.AccountStatus status,
            Pageable pageable
    );
}
