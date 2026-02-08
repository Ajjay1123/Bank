package com.bank.util;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class AccountNumberGenerator {
    
    private static final SecureRandom random = new SecureRandom();
    private static final String PREFIX = "ACC";
    
    public static String generateAccountNumber() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmss"));
        int randomNum = 1000 + random.nextInt(9000); // 4-digit random number
        return PREFIX + timestamp + randomNum;
    }
    
    public static String generateTransactionId() {
        String timestamp = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMddHHmmssSSS"));
        int randomNum = 10000 + random.nextInt(90000); // 5-digit random number
        return "TXN" + timestamp + randomNum;
    }
}
