package edu.sia.asignatura.application.service;

import edu.sia.asignatura.application.dto.AsignaturaDto;
import edu.sia.asignatura.application.dto.CreateAsignaturaDto;
import edu.sia.asignatura.application.dto.UpdateAsignaturaDto;
import edu.sia.asignatura.domain.entity.Asignatura;
import edu.sia.asignatura.domain.repository.AsignaturaRepository;
import edu.sia.exception.BusinessException;
import edu.sia.exception.ConflictException;
import edu.sia.exception.ResourceNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class AsignaturaService {

    private final AsignaturaRepository asignaturaRepository;

    @Transactional
    public Mono<AsignaturaDto> createAsignatura(CreateAsignaturaDto dto) {
        log.info("Creating asignatura with codigo: {}", dto.getCodigo());

        return asignaturaRepository.existsByCodigo(dto.getCodigo())
                .flatMap(exists -> {
                    if (exists) {
                        return Mono.error(new ConflictException(
                                "Ya existe una asignatura con el código: " + dto.getCodigo()));
                    }

                    Asignatura asignatura = Asignatura.builder()
                            .codigo(dto.getCodigo())
                            .nombre(dto.getNombre())
                            .creditos(dto.getCreditos())
                            .descripcion(dto.getDescripcion())
                            .estado("ACTIVE")
                            .tenantId(dto.getTenantId())
                            .build();

                    return asignaturaRepository.save(asignatura)
                            .map(this::toDto);
                });
    }

    public Mono<AsignaturaDto> getAsignaturaById(Long asignaturaId) {
        log.info("Getting asignatura by id: {}", asignaturaId);

        return asignaturaRepository.findById(asignaturaId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Asignatura no encontrada con ID: " + asignaturaId)))
                .map(this::toDto);
    }

    public Mono<AsignaturaDto> getAsignaturaByCodigo(String codigo) {
        log.info("Getting asignatura by codigo: {}", codigo);

        return asignaturaRepository.findByCodigo(codigo)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Asignatura no encontrada con código: " + codigo)))
                .map(this::toDto);
    }

    public Flux<AsignaturaDto> getAllAsignaturas() {
        log.info("Getting all asignaturas");

        return asignaturaRepository.findAll()
                .map(this::toDto);
    }

    public Flux<AsignaturaDto> getAsignaturasByTenantId(Long tenantId) {
        log.info("Getting asignaturas for tenant: {}", tenantId);

        return asignaturaRepository.findByTenantId(tenantId)
                .map(this::toDto);
    }

    public Flux<AsignaturaDto> getActiveAsignaturasByTenantId(Long tenantId) {
        log.info("Getting active asignaturas for tenant: {}", tenantId);

        return asignaturaRepository.findActiveByTenantId(tenantId)
                .map(this::toDto);
    }

    @Transactional
    public Mono<AsignaturaDto> updateAsignatura(Long asignaturaId, UpdateAsignaturaDto dto) {
        log.info("Updating asignatura: {}", asignaturaId);

        return asignaturaRepository.findById(asignaturaId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Asignatura no encontrada con ID: " + asignaturaId)))
                .flatMap(asignatura -> {
                    asignatura.setNombre(dto.getNombre());
                    asignatura.setCreditos(dto.getCreditos());
                    asignatura.setDescripcion(dto.getDescripcion());

                    return asignaturaRepository.save(asignatura)
                            .map(this::toDto);
                });
    }

    @Transactional
    public Mono<Void> deleteAsignatura(Long asignaturaId) {
        log.info("Deleting asignatura: {}", asignaturaId);

        return asignaturaRepository.findById(asignaturaId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Asignatura no encontrada con ID: " + asignaturaId)))
                .flatMap(asignatura -> {
                    // Soft delete: cambiar estado a INACTIVE
                    asignatura.setEstado("INACTIVE");
                    return asignaturaRepository.save(asignatura);
                })
                .then();
    }

    @Transactional
    public Mono<Void> activateAsignatura(Long asignaturaId) {
        log.info("Activating asignatura: {}", asignaturaId);

        return asignaturaRepository.findById(asignaturaId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Asignatura no encontrada con ID: " + asignaturaId)))
                .flatMap(asignatura -> {
                    asignatura.setEstado("ACTIVE");
                    return asignaturaRepository.save(asignatura);
                })
                .then();
    }

    private AsignaturaDto toDto(Asignatura asignatura) {
        return AsignaturaDto.builder()
                .asignaturaId(asignatura.getAsignaturaId())
                .codigo(asignatura.getCodigo())
                .nombre(asignatura.getNombre())
                .creditos(asignatura.getCreditos())
                .descripcion(asignatura.getDescripcion())
                .estado(asignatura.getEstado())
                .tenantId(asignatura.getTenantId())
                .createdAt(asignatura.getCreatedAt())
                .updatedAt(asignatura.getUpdatedAt())
                .build();
    }
}
