package edu.sia.grupo.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class GrupoConEstudiantesDto {

    private Long grupoId;
    private String asignaturaNombre;
    private String asignaturaCodigo;
    private String periodo;
    private Integer cupo;
    private Integer matriculados;
    private String horario;
    private List<EstudianteMatriculadoDto> estudiantes;
}

