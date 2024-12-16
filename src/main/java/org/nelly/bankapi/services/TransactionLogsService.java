package org.nelly.bankapi.services;

import com.alibaba.fastjson2.JSONArray;

import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.models.Account;
import org.nelly.bankapi.models.TransactionLogs;
import org.nelly.bankapi.models.enums.TransactionMetric;
import org.nelly.bankapi.models.enums.TransactionType;
import org.nelly.bankapi.repositories.AccountRepository;
import org.nelly.bankapi.repositories.TransactionLogsRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;

@Service
@RequiredArgsConstructor
public class TransactionLogsService {

    private final TransactionLogsRepository transactionLogsRepository;
    private final AccountRepository accountRepository;

    public void logTransaction(Account account, Double amount, TransactionType transactionType) {
        TransactionLogs transactionLogs = TransactionLogs.builder()
                .account(account)
                .accountNumber(account.getAccountNumber())
                .amount(amount)
                .transactionType(transactionType)
                .transactionDate(new Timestamp(System.currentTimeMillis()))
                .balanceBefore(account.getBalance())
                .build();
        if (transactionType.getTransactionMetric() == TransactionMetric.DEBIT) {
            transactionLogs.setBalanceAfter(account.getBalance() - amount);
            transactionLogs.setDescription("Debit transaction of " + amount + " " + account.getCurrency().name());
        } else if (transactionType.getTransactionMetric() == TransactionMetric.CREDIT) {
            transactionLogs.setBalanceAfter(account.getBalance() + amount);
            transactionLogs.setDescription("Credit transaction of " + amount + " " + account.getCurrency().name());
        }
        transactionLogsRepository.save(transactionLogs);
        account.setBalance(transactionLogs.getBalanceAfter());
        accountRepository.save(account);
    }

    public JSONArray listTransactionLogs() {
        return JSONArray.from(transactionLogsRepository.findAll());
    }
}
