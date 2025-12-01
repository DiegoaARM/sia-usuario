package edu.sia.estudiante.infrastructure.controller;

import edu.sia.nota.application.dto.CalificacionPorAsignaturaDto;
import edu.sia.nota.application.dto.DetalleCalificacionDto;
import edu.sia.nota.application.service.NotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/estudiantes")
@RequiredArgsConstructor
@Tag(name = "Estudiantes", description = "API para consultas de estudiantes")
public class EstudianteController {

    private final NotaService notaService;

    @GetMapping("/{estudianteId}/calificaciones")
    @Operation(summary = "Consultar calificaciones",
               description = "HU-021: Obtener calificaciones por asignatura y periodo con nota final consolidada")
    public Flux<CalificacionPorAsignaturaDto> getCalificaciones(@PathVariable Long estudianteId) {
        return notaService.getCalificacionesByEstudiante(estudianteId);
    }

    @GetMapping("/{estudianteId}/calificaciones/{asignaturaId}")
    @Operation(summary = "Detalle de calificación",
               description = "HU-023: Obtener detalle de notas por componente con cálculo de nota final")
    public Mono<DetalleCalificacionDto> getDetalleCalificacion(
            @PathVariable Long estudianteId,
            @PathVariable Long asignaturaId) {
        return notaService.getDetalleCalificacion(estudianteId, asignaturaId);
    }
}

