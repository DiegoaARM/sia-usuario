package edu.sia.matricula.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.time.LocalDateTime;

@Table("matriculas")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Matricula {

    @Id
    @Column("matricula_id")
    private Long matriculaId;

    @Column("estudiante_id")
    private Long estudianteId;

    @Column("grupo_id")
    private Long grupoId;

    @Column("fecha_matricula")
    private LocalDateTime fechaMatricula;

    @Column("estado")
    private String estado;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
}

