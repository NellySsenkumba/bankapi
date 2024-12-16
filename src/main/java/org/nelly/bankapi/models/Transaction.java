package org.nelly.bankapi.models;

import jakarta.persistence.*;
import lombok.Data;
import org.nelly.bankapi.models.enums.TransactionStatus;
import org.nelly.bankapi.models.enums.TransactionType;

import java.sql.Timestamp;

@Entity
@Data
@Table(name = "transactions")
public class Transaction {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "transaction_id_seq")
    @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
    private Long id;
    private Long accountId;
    private Double amount;
    private Timestamp date;
    private String description;
    private TransactionStatus status;
    private TransactionType type;

}
