package org.nelly.bankapi.services;

import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.models.Account;
import org.nelly.bankapi.models.enums.TransactionType;
import org.nelly.bankapi.repositories.AccountRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TransactionService implements IService {

    private final AccountRepository accountRepository;
    private final TransactionLogsService transactionLogsService;

    public JSONObject depositMoney(JSONObject request) {
        requires(List.of("accountNumber", "amount"), request);
        Account account = accountRepository.findAccountByAccountNumber(request.getLong("accountNumber")).orElseThrow();
        transactionLogsService.logTransaction(account, request.getDouble("amount"), TransactionType.DEPOSIT);
        return JSONObject.of("message", "deposit successful", "accountDetails", account);
    }

    public JSONObject withdrawMoney(JSONObject request) {
        requires(List.of("accountNumber", "amount"), request);
        Account account = accountRepository.findAccountByAccountNumber(request.getLong("accountNumber")).orElseThrow(() -> new RuntimeException("Invalid Account Number"));
        if (account.getBalance() < request.getDouble("amount")) {
            throw new RuntimeException("Insufficient balance");
        }
        transactionLogsService.logTransaction(account, request.getDouble("amount"), TransactionType.WITHDRAWAL_AGENT);
        return JSONObject.of("message", "withdraw successful", "accountDetails", account);
    }

    public JSONObject transferMoney(JSONObject request) {
        requires(List.of("senderAccountNumber", "receiverAccountNumber", "amount"), request);
        Account sender = accountRepository.findAccountByAccountNumber(request.getLong("senderAccountNumber")).orElseThrow(() -> new RuntimeException("Invalid Sender Account Number"));
        Account receiver = accountRepository.findAccountByAccountNumber(request.getLong("receiverAccountNumber")).orElseThrow(() -> new RuntimeException("Invalid Receiver Account Number"));
        Double amount = request.getDouble("amount");

        if (sender.getBalance() < amount) {
            throw new RuntimeException("Insufficient balance");
        }

        transactionLogsService.logTransaction(sender, amount, TransactionType.INTERNAL_TRANSFER_SENDER);
        transactionLogsService.logTransaction(receiver, amount, TransactionType.INTERNAL_TRANSFER_RECEIVER);
        return JSONObject.of("message", "transfer successful", "senderAccountDetails", sender.getMaskedAccountNumber(), "receiverAccountDetails", receiver.getMaskedAccountNumber());
    }
}
