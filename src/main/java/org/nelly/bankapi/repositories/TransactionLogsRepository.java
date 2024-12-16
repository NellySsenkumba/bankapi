package org.nelly.bankapi.repositories;

import org.nelly.bankapi.models.TransactionLogs;
import org.springframework.data.jpa.repository.JpaRepository;

import java.sql.Timestamp;
import java.util.List;

public interface TransactionLogsRepository extends JpaRepository<TransactionLogs, Long> {
    List<TransactionLogs> findByAccount_IdAndTransactionDateBetween(Long accountId, Timestamp startDate, Timestamp endDate);

    List<TransactionLogs> findByAccount_Id(Long id);
}
