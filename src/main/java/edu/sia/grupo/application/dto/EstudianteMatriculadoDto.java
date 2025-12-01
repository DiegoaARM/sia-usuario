package edu.sia.grupo.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteMatriculadoDto {

    private Long estudianteId;
    private String nombreCompleto;
    private String email;
    private String idNumber;
    private LocalDateTime fechaMatricula;
    private String estado;
}

