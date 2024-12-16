package org.nelly.bankapi.services;

import com.alibaba.fastjson2.JSONArray;
import com.alibaba.fastjson2.JSONObject;
import lombok.RequiredArgsConstructor;
import org.nelly.bankapi.generators.AccountHelper;
import org.nelly.bankapi.models.Account;
import org.nelly.bankapi.models.enums.AccountStatus;
import org.nelly.bankapi.models.enums.AccountType;
import org.nelly.bankapi.models.enums.Currency;
import org.nelly.bankapi.models.enums.TransactionType;
import org.nelly.bankapi.repositories.AccountRepository;
import org.nelly.bankapi.repositories.CustomerRepository;
import org.nelly.bankapi.repositories.TransactionLogsRepository;
import org.springframework.stereotype.Service;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AccountService implements IService {
    private final AccountRepository accountRepository;
    private final CustomerRepository customerRepository;
    private final TransactionLogsService transactionLogsService;
    private final TransactionLogsRepository transactionLogsRepository;

    public JSONObject createAccount(JSONObject accountDetails) {
        requires(
                List.of("customerId", "accountType", "currency", "startingAmount"),
                accountDetails
        );
        Long accountNumber = AccountHelper.generateAccountNumber();
        Account account = Account.builder()
                .accountNumber(accountNumber)
                .accountType(AccountType.valueOf(accountDetails.getString("accountType")))
                .customerId(customerRepository.findById(accountDetails.getLong("customerId")).orElseThrow())
                .balance(0.0)
                .currency(Currency.valueOf(accountDetails.getString("currency")))
                .status(AccountStatus.ACTIVE)
                .createdAt(new Timestamp(System.currentTimeMillis()))
                .build();
        account = accountRepository.save(account);
        transactionLogsService.logTransaction(account, accountDetails.getDouble("startingAmount"), TransactionType.DEPOSIT);
        return JSONObject.of("message", "account created with account number" + accountNumber, "accountDetails", account);
    }

    public JSONArray listAccount() {
        return JSONArray.from(accountRepository.findAll());
    }

    public JSONArray accountLogs(Long id, String startDate, String endDate) {
        if (startDate == null || endDate == null || startDate.isEmpty() || endDate.isEmpty()) {
            return JSONArray.from(transactionLogsRepository.findByAccount_Id(id));
        }
        Timestamp startDateTimestamp = Timestamp.valueOf(LocalDate.parse(startDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay());
        Timestamp endDateTimestamp = Timestamp.valueOf(LocalDate.parse(endDate, DateTimeFormatter.ofPattern("dd/MM/yyyy")).atStartOfDay());
        return JSONArray.from(transactionLogsRepository.findByAccount_IdAndTransactionDateBetween(id, startDateTimestamp, endDateTimestamp));
    }

    public JSONObject singleAccount(Long id) {
        return JSONObject.of("account", accountRepository.findById(id).orElseThrow());
    }
}
