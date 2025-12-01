package edu.sia.grupo.application.dto;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignarDocenteDto {

    @NotNull(message = "El ID del docente es obligatorio")
    private Long docenteId;
}

