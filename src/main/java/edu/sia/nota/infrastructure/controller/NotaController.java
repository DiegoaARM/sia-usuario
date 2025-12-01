package edu.sia.nota.infrastructure.controller;

import edu.sia.nota.application.dto.*;
import edu.sia.nota.application.service.NotaService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
@Tag(name = "Notas", description = "API para gestión de notas y calificaciones")
public class NotaController {

    private final NotaService notaService;

    @PostMapping("/grupos/{grupoId}/notas")
    @ResponseStatus(HttpStatus.CREATED)
    @Operation(summary = "Ingresar nota",
               description = "HU-010: Registrar una nota para un estudiante en un componente evaluativo")
    public Mono<NotaDto> createNota(
            @PathVariable Long grupoId,
            @Valid @RequestBody CreateNotaDto dto) {
        return notaService.createNota(grupoId, dto);
    }

    @PutMapping("/notas/{notaId}")
    @Operation(summary = "Modificar nota",
               description = "HU-013: Actualizar una nota con justificación obligatoria")
    public Mono<NotaDto> updateNota(
            @PathVariable Long notaId,
            @Valid @RequestBody UpdateNotaDto dto,
            Authentication authentication) {
        // En un sistema real, extraerías el ID del usuario del token JWT
        // Por ahora usaremos un valor fijo o desde el contexto de seguridad
        Long modificadoPor = 1L; // Esto debería venir del usuario autenticado
        return notaService.updateNota(notaId, dto, modificadoPor);
    }
}

