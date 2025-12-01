package edu.sia.asignatura.infrastructure.controller;

import edu.sia.asignatura.application.dto.AsignaturaDto;
import edu.sia.asignatura.application.dto.CreateAsignaturaDto;
import edu.sia.asignatura.application.dto.UpdateAsignaturaDto;
import edu.sia.asignatura.application.service.AsignaturaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/asignaturas")
@RequiredArgsConstructor
@Tag(name = "Asignaturas", description = "API para gestión de asignaturas")
public class AsignaturaController {

    private final AsignaturaService asignaturaService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Crear asignatura", description = "Registrar una nueva asignatura en el sistema")
    public Mono<AsignaturaDto> createAsignatura(@RequestBody CreateAsignaturaDto dto) {
        return asignaturaService.createAsignatura(dto);
    }

    @GetMapping("/{asignaturaId}")
    @Operation(summary = "Obtener asignatura", description = "Consultar información de una asignatura por ID")
    public Mono<AsignaturaDto> getAsignaturaById(@PathVariable Long asignaturaId) {
        return asignaturaService.getAsignaturaById(asignaturaId);
    }

    @GetMapping("/codigo/{codigo}")
    @Operation(summary = "Buscar por código", description = "Consultar asignatura por su código único")
    public Mono<AsignaturaDto> getAsignaturaByCodigo(@PathVariable String codigo) {
        return asignaturaService.getAsignaturaByCodigo(codigo);
    }

    @GetMapping
    @Operation(summary = "Listar todas", description = "Obtener listado de todas las asignaturas")
    public Flux<AsignaturaDto> getAllAsignaturas() {
        return asignaturaService.getAllAsignaturas();
    }

    @GetMapping("/tenant/{tenantId}")
    @Operation(summary = "Listar por tenant", description = "Obtener asignaturas de un tenant específico")
    public Flux<AsignaturaDto> getAsignaturasByTenant(@PathVariable Long tenantId) {
        return asignaturaService.getAsignaturasByTenantId(tenantId);
    }

    @GetMapping("/tenant/{tenantId}/active")
    @Operation(summary = "Listar activas por tenant", description = "Obtener solo asignaturas activas de un tenant")
    public Flux<AsignaturaDto> getActiveAsignaturasByTenant(@PathVariable Long tenantId) {
        return asignaturaService.getActiveAsignaturasByTenantId(tenantId);
    }

    @PutMapping("/{asignaturaId}")
    @Operation(summary = "Actualizar asignatura", description = "Modificar información de una asignatura existente")
    public Mono<AsignaturaDto> updateAsignatura(
            @PathVariable Long asignaturaId,
            @Valid @RequestBody UpdateAsignaturaDto dto) {
        return asignaturaService.updateAsignatura(asignaturaId, dto);
    }

    @DeleteMapping("/{asignaturaId}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @Operation(summary = "Eliminar asignatura", description = "Desactivar una asignatura (soft delete)")
    public Mono<Void> deleteAsignatura(@PathVariable Long asignaturaId) {
        return asignaturaService.deleteAsignatura(asignaturaId);
    }

    @PatchMapping("/{asignaturaId}/activate")
    @Operation(summary = "Activar asignatura", description = "Reactivar una asignatura previamente desactivada")
    public Mono<Void> activateAsignatura(@PathVariable Long asignaturaId) {
        return asignaturaService.activateAsignatura(asignaturaId);
    }
}
