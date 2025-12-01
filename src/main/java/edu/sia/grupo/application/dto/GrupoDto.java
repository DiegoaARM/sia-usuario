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
public class GrupoDto {

    private Long grupoId;
    private Long asignaturaId;
    private String asignaturaNombre;
    private String asignaturaCodigo;
    private Long docenteId;
    private String docenteNombre;
    private String periodo;
    private Integer cupo;
    private Integer matriculados;
    private String horario;
    private String estado;
    private LocalDateTime createdAt;
}

