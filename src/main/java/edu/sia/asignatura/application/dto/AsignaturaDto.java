package edu.sia.asignatura.application.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class AsignaturaDto {

    private Long asignaturaId;
    private String codigo;
    private String nombre;
    private Integer creditos;
    private String descripcion;
    private String estado;
    private Long tenantId;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
