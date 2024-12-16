package org.nelly.bankapi.models;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "roles")
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "role_gen")
    @SequenceGenerator(
            name = "role_gen",
            sequenceName = "role_sequence_id",
            allocationSize = 1
    )
    private Long id;
    private String name;
    private String code;
}
