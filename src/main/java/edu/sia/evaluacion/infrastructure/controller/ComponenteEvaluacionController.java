package edu.sia.evaluacion.infrastructure.controller;

import edu.sia.evaluacion.application.dto.ComponenteEvaluacionDto;
import edu.sia.evaluacion.application.dto.CreateComponenteEvaluacionDto;
import edu.sia.evaluacion.application.service.ComponenteEvaluacionService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/asignaturas/{asignaturaId}/evaluacion")
@RequiredArgsConstructor
@Tag(name = "Evaluación", description = "API para gestión de componentes de evaluación")
public class ComponenteEvaluacionController {

    private final ComponenteEvaluacionService componenteService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Configurar evaluación",
               description = "HU-007: Registrar componentes evaluativos para una asignatura")
    public Mono<ComponenteEvaluacionDto> createComponente(
            @PathVariable Long asignaturaId,
            @Valid @RequestBody CreateComponenteEvaluacionDto dto) {
        return componenteService.createComponente(asignaturaId, dto);
    }

    @GetMapping
    @Operation(summary = "Listar componentes", description = "Obtener todos los componentes de evaluación de una asignatura")
    public Flux<ComponenteEvaluacionDto> getComponentes(@PathVariable Long asignaturaId) {
        return componenteService.getComponentesByAsignaturaId(asignaturaId);
    }
}

