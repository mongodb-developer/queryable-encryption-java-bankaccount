package com.mongodb.bankaccount.domain;

import java.util.List;

public interface BankAccountPort {
    String insert(BankAccount bankAccount);
    List<BankAccount> find();
    List<BankAccount> balanceGreaterThan(double value);

    BankAccount findByAccountNumber(String accountNumber);
}
