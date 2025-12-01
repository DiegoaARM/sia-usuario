package edu.sia.docente.infrastructure.controller;

import edu.sia.grupo.application.dto.GrupoConEstudiantesDto;
import edu.sia.grupo.application.service.GrupoService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;

@RestController
@RequestMapping("/api/docentes")
@RequiredArgsConstructor
@Tag(name = "Docentes", description = "API para consultas de docentes")
public class DocenteController {

    private final GrupoService grupoService;

    @GetMapping("/{docenteId}/grupos")
    @Operation(summary = "Ver grupos asignados",
               description = "HU-009: Obtener lista de grupos del docente con estudiantes matriculados")
    public Flux<GrupoConEstudiantesDto> getGruposConEstudiantes(@PathVariable Long docenteId) {
        return grupoService.getGruposByDocenteId(docenteId);
    }
}

