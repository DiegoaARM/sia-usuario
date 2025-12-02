package edu.sia.grupo.application.dto;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CreateGrupoDto {

    @NotNull(message = "El ID de la asignatura es obligatorio")
    private Long asignaturaId;


    @NotBlank(message = "El periodo es obligatorio")
    @Pattern(regexp = "\\d{4}-[12]", message = "El periodo debe tener el formato YYYY-1 o YYYY-2")
    private String periodo;

    @NotNull(message = "El cupo es obligatorio")
    @Min(value = 1, message = "El cupo debe ser mayor a 0")
    @Max(value = 100, message = "El cupo no puede exceder 100 estudiantes")
    private Integer cupo;

    @NotBlank(message = "El horario es obligatorio")
    @Size(max = 255, message = "El horario no puede exceder 255 caracteres")
    private String horario;
}


