package org.nelly.bankapi.models;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.nelly.bankapi.models.enums.AccountStatus;
import org.nelly.bankapi.models.enums.AccountType;
import org.nelly.bankapi.models.enums.Currency;

import java.sql.Timestamp;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "accounts")
public class Account {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "account_id_seq")
    @SequenceGenerator(name = "account_id_seq", sequenceName = "account_id_seq", allocationSize = 1)
    private Long id;
    @ManyToOne
    @JoinColumn(name = "customer_id")
    private Customer customerId;
    private AccountType accountType;
    private Double balance;
    private Currency currency;
    private AccountStatus status;
    @CreationTimestamp
    private Timestamp createdAt;
    @Column(unique = true)
    private Long accountNumber;

    public String getMaskedAccountNumber() {
        return String.valueOf(accountNumber).replaceAll("\\d(?=\\d{4})", "*");
    }
}
