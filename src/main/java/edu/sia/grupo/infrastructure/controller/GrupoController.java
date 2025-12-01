package edu.sia.grupo.infrastructure.controller;

import edu.sia.grupo.application.dto.AsignarDocenteDto;
import edu.sia.grupo.application.dto.CreateGrupoDto;
import edu.sia.grupo.application.dto.GrupoDto;
import edu.sia.grupo.application.service.GrupoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/grupos")
@RequiredArgsConstructor
@Tag(name = "Grupos", description = "API para gestión de grupos")
public class GrupoController {

    private final GrupoService grupoService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear grupo", description = "HU-004: Crear un grupo para una asignatura")
    public Mono<GrupoDto> createGrupo(@Valid @RequestBody CreateGrupoDto dto) {
        return grupoService.createGrupo(dto);
    }

    @PutMapping("/{grupoId}/docente")
    @Operation(summary = "Asignar docente", description = "HU-005: Asignar o cambiar el docente responsable de un grupo")
    public Mono<GrupoDto> asignarDocente(
            @PathVariable Long grupoId,
            @Valid @RequestBody AsignarDocenteDto dto) {
        return grupoService.asignarDocente(grupoId, dto);
    }

    @GetMapping("/{grupoId}")
    @Operation(summary = "Obtener grupo", description = "Obtener información de un grupo por ID")
    public Mono<GrupoDto> getGrupoById(@PathVariable Long grupoId) {
        return grupoService.getGrupoById(grupoId);
    }
}


