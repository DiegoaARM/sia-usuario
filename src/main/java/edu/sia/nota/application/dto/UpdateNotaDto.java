package edu.sia.nota.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UpdateNotaDto {

    @NotNull(message = "El nuevo valor de la nota es obligatorio")
    @DecimalMin(value = "0.0", message = "La nota debe ser mayor o igual a 0.0")
    @DecimalMax(value = "5.0", message = "La nota no puede exceder 5.0")
    @Digits(integer = 1, fraction = 2, message = "La nota debe tener máximo 1 dígito entero y 2 decimales")
    private BigDecimal valor;

    @NotBlank(message = "La justificación es obligatoria")
    @Size(min = 10, max = 500, message = "La justificación debe tener entre 10 y 500 caracteres")
    private String justificacion;
}

