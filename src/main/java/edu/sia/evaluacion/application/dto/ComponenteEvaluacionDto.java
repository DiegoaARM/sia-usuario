package edu.sia.evaluacion.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class ComponenteEvaluacionDto {

    private Long componenteId;
    private Long asignaturaId;
    private String nombre;
    private BigDecimal porcentaje;
    private String descripcion;
    private String estado;
    private LocalDateTime createdAt;
}

