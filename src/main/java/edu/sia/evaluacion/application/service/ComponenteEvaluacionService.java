package edu.sia.evaluacion.application.service;

import edu.sia.asignatura.domain.repository.AsignaturaRepository;
import edu.sia.evaluacion.application.dto.ComponenteEvaluacionDto;
import edu.sia.evaluacion.application.dto.CreateComponenteEvaluacionDto;
import edu.sia.evaluacion.domain.entity.ComponenteEvaluacion;
import edu.sia.evaluacion.domain.repository.ComponenteEvaluacionRepository;
import edu.sia.exception.BusinessException;
import edu.sia.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class ComponenteEvaluacionService {

    private final ComponenteEvaluacionRepository componenteRepository;
    private final AsignaturaRepository asignaturaRepository;

    @Transactional
    public Mono<ComponenteEvaluacionDto> createComponente(Long asignaturaId, CreateComponenteEvaluacionDto dto) {
        log.info("Creating componente evaluacion for asignatura: {}", asignaturaId);

        return asignaturaRepository.findById(asignaturaId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Asignatura no encontrada con ID: " + asignaturaId)))
                .flatMap(asignatura -> componenteRepository.sumPorcentajesByAsignaturaId(asignaturaId)
                        .flatMap(sumPorcentajes -> {
                            BigDecimal nuevoTotal = sumPorcentajes.add(dto.getPorcentaje());

                            if (nuevoTotal.compareTo(BigDecimal.valueOf(100)) > 0) {
                                return Mono.error(new BusinessException(
                                        "La suma de porcentajes no puede exceder 100%. " +
                                        "Actual: " + sumPorcentajes + "%, " +
                                        "Nuevo componente: " + dto.getPorcentaje() + "%"));
                            }

                            ComponenteEvaluacion componente = ComponenteEvaluacion.builder()
                                    .asignaturaId(asignaturaId)
                                    .nombre(dto.getNombre())
                                    .porcentaje(dto.getPorcentaje())
                                    .descripcion(dto.getDescripcion())
                                    .estado("ACTIVE")
                                    .build();

                            return componenteRepository.save(componente)
                                    .map(this::toDto);
                        }));
    }

    public Flux<ComponenteEvaluacionDto> getComponentesByAsignaturaId(Long asignaturaId) {
        return asignaturaRepository.findById(asignaturaId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Asignatura no encontrada con ID: " + asignaturaId)))
                .flatMapMany(asignatura -> componenteRepository.findActiveByAsignaturaId(asignaturaId)
                        .map(this::toDto));
    }

    public Mono<BigDecimal> validatePorcentajesTotal(Long asignaturaId) {
        return componenteRepository.sumPorcentajesByAsignaturaId(asignaturaId)
                .map(sum -> {
                    if (sum.compareTo(BigDecimal.valueOf(100)) != 0) {
                        throw new BusinessException(
                                "La suma de porcentajes debe ser 100%. Actual: " + sum + "%");
                    }
                    return sum;
                });
    }

    private ComponenteEvaluacionDto toDto(ComponenteEvaluacion componente) {
        return ComponenteEvaluacionDto.builder()
                .componenteId(componente.getComponenteId())
                .asignaturaId(componente.getAsignaturaId())
                .nombre(componente.getNombre())
                .porcentaje(componente.getPorcentaje())
                .descripcion(componente.getDescripcion())
                .estado(componente.getEstado())
                .createdAt(componente.getCreatedAt())
                .build();
    }
}

