package edu.sia.nota.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DetalleCalificacionDto {

    private Long asignaturaId;
    private String asignaturaNombre;
    private String asignaturaCodigo;
    private String periodo;
    private List<NotaPorComponenteDto> componentes;
    private BigDecimal notaFinal;
}

