package edu.sia.users.domain.entity;

import edu.sia.tenant.domain.entity.Tenant;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Table(name = "usuarios")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long usuarioId;

    @Column(unique = true)
    private String email;

    @Column(nullable = false)
    private String givenName;

    @Column(nullable = false)
    private String familyName;

    @Column(nullable = false)
    private String idType; // Tipo de documento ('CC', 'CE', 'PP', etc.)

    @Column(nullable = false)
    private String idNumber; // Número de documento

    @Column(nullable = false)
    private String roleName;

    @Column(nullable = false)
    private String phoneNumber;

    @Column(nullable = false)
    private LocalDate birthdate;

    @Column(nullable = false)
    private String genre; // 'M' para masculino, 'F' para femenino, no binario, etc.

    @Column(nullable = false)
    private String state; // 'ACTIVE', 'INACTIVE', etc.

    private LocalDateTime lastLogin;

    @CreationTimestamp
    private LocalDateTime createdAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tenant_id")
    private Tenant tenantId;
}
