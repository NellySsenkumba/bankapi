package org.nelly.bankapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nelly.bankapi.models.enums.TransactionType;

import java.sql.Timestamp;

@Entity
@Data
@AllArgsConstructor
@Builder
@NoArgsConstructor
public class TransactionLogs {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "transaction_id_seq")
    @SequenceGenerator(name = "transaction_id_seq", sequenceName = "transaction_id_seq", allocationSize = 1)
    private Long id;
    private Long accountNumber;
    private TransactionType transactionType;
    @ManyToOne
    @JsonIgnore
    private Account account;
    private Double amount;
    private String description;
    private String reference;
    private Timestamp transactionDate;
    private Double balanceBefore;
    private Double balanceAfter;

}
