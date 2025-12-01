package edu.sia.tenant.domain.entity;

import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Table(name = "tenants")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Tenant {
    @Id
    private Long tenantId;

    @Column("name")
    private String name;

    @Column("domain")
    private String domain;

    @Column("email")
    private String email;

    @Column("phone_number")
    private String phoneNumber;

    @Column("country")
    private String country;

    @Column("language")
    private String language;

    @Column("timezone")
    private String timezone;

    @Column("state")
    private String state;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}
