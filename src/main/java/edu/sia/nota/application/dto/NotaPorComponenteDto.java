package edu.sia.nota.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotaPorComponenteDto {

    private Long componenteId;
    private String componenteNombre;
    private BigDecimal porcentaje;
    private BigDecimal valor;
    private BigDecimal valorPonderado;
}

