package org.nelly.bankapi.models;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;
import org.nelly.bankapi.models.enums.AUTHORITY;
import org.nelly.bankapi.models.enums.Gender;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Collection;
import java.util.List;

@Entity
@Builder
@Data
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "customers")
public class Customer implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY, generator = "customer_id_seq")
    @SequenceGenerator(name = "customer_id_seq", sequenceName = "customer_id_seq", allocationSize = 1)
    private Long id;
    private String firstName;
    private String lastName;
    private LocalDate dob;
    private String address;
    private String phoneNumber;
    @Column(unique = true, nullable = false)
    private String email;
    @CreationTimestamp
    private Timestamp createdAt;
    private String nin;
    @Enumerated(EnumType.STRING)
    private Gender gender;
    @Column(unique = true)
    private String username;
    @JsonIgnore
    private String password;
    private Timestamp lastLoggedInAt;

    @Override
    @JsonIgnore
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(AUTHORITY.CUSTOMER.name()));
    }

    public String getUsername() {
        return username != null ? this.username : this.email;
    }
}
