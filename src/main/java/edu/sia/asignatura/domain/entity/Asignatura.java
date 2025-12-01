package edu.sia.asignatura.domain.entity;

import org.springframework.data.relational.core.mapping.Table;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.CreatedDate;
import lombok.NoArgsConstructor;
import lombok.Data;
import lombok.Builder;
import lombok.AllArgsConstructor;


import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("asignaturas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Asignatura {

    @Id
    @Column("asignatura_id")
    private Long asignaturaId;

    @Column("codigo")
    private String codigo;

    @Column("nombre")
    private String nombre;

    @Column("creditos")
    private Integer creditos;

    @Column("descripcion")
    private String descripcion;

    @Column("estado")
    private String estado;

    @Column("tenant_id")
    private Long tenantId;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}



