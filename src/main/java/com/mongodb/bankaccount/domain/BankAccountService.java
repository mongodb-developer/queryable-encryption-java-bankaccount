package com.mongodb.bankaccount.domain;

import com.mongodb.bankaccount.resources.BankAccountRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Objects;

@Service
public class BankAccountService {
    private static final Logger logger = LoggerFactory.getLogger(BankAccountService.class);

    BankAccountRepository bankAccountRepository;

    BankAccountService(BankAccountRepository bankAccountRepository) {
        this.bankAccountRepository = bankAccountRepository;
    }

    public String insert(BankAccount bankAccount) {
        Objects.requireNonNull(bankAccount, "Bank account must not be null");
        return bankAccountRepository.insert(bankAccount);
     }

    public List<BankAccount> find() {
        return bankAccountRepository.find();
    }

    public List<BankAccount> balanceGreaterThan(double value) {
        return bankAccountRepository.balanceGreaterThan(value);
    }

    public BankAccount findByAccountNumber(String accountNumber) {
        return bankAccountRepository.findByAccountNumber(accountNumber);
    }

}



