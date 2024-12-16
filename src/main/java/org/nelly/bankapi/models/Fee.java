package org.nelly.bankapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.nelly.bankapi.models.enums.TransactionType;

@Entity
@Data
@Table(name = "fees")
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Fee {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "fee_id_seq")
    @SequenceGenerator(name = "fee_id_seq", sequenceName = "fee_id_seq", allocationSize = 1)
    private Long id;
    private Double amount;
    private String description;
    @Enumerated(EnumType.STRING)
    private TransactionType type;
}
