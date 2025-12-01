package edu.sia.nota.application.dto;

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
public class NotaDto {

    private Long notaId;
    private Long matriculaId;
    private Long estudianteId;
    private String estudianteNombre;
    private Long componenteId;
    private String componenteNombre;
    private BigDecimal componentePorcentaje;
    private BigDecimal valor;
    private String justificacion;
    private LocalDateTime fechaRegistro;
    private LocalDateTime fechaModificacion;
    private Long modificadoPor;
    private String modificadoPorNombre;
}

