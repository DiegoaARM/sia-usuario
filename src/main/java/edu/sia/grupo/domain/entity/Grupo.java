package edu.sia.grupo.domain.entity;

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

@Table("grupos")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Grupo {

    @Id
    @Column("grupo_id")
    private Long grupoId;

    @Column("asignatura_id")
    private Long asignaturaId;

    @Column("docente_id")
    private Long docenteId;

    @Column("periodo")
    private String periodo;

    @Column("cupo")
    private Integer cupo;

    @Column("horario")
    private String horario;

    @Column("estado")
    private String estado;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;

    @LastModifiedDate
    @Column("updated_at")
    private LocalDateTime updatedAt;
}

