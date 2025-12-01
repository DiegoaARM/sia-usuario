package edu.sia.evaluacion.domain.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.Id;
import org.springframework.data.relational.core.mapping.Column;
import org.springframework.data.relational.core.mapping.Table;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Table("componentes_evaluacion")
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponenteEvaluacion {

    @Id
    @Column("componente_id")
    private Long componenteId;

    @Column("asignatura_id")
    private Long asignaturaId;

    @Column("nombre")
    private String nombre;

    @Column("porcentaje")
    private BigDecimal porcentaje;

    @Column("descripcion")
    private String descripcion;

    @Column("estado")
    private String estado;

    @CreatedDate
    @Column("created_at")
    private LocalDateTime createdAt;
}

