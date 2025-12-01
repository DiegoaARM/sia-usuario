package edu.sia.users.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Table(name = "\"usuarios\"")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {
    @Id
    private Long usuarioId;

    @Column("email")
    private String email;

    @Column("given_name")
    private String givenName;

    @Column("family_name")
    private String familyName;

    @Column("id_type")
    private String idType; // Tipo de documento ('CC', 'CE', 'PP', etc.)

    @Column("id_number")
    private String idNumber; // Número de documento

    @Column("role_name")
    private String roleName;

    @Column("phone_number")
    private String phoneNumber;

    @Column("birthdate")
    private LocalDate birthdate;

    @Column("genre")
    private String genre; // 'M' para masculino, 'F' para femenino, no binario, etc.

    @Column("state")
    private String state; // 'ACTIVE', 'INACTIVE', etc.

    @Column("last_login")
    private LocalDateTime lastLogin;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @Column("tenant_id")
    private Long tenantId;
}
