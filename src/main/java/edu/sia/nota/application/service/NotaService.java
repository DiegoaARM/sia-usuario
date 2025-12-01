package edu.sia.nota.application.service;

import edu.sia.asignatura.domain.repository.AsignaturaRepository;
import edu.sia.evaluacion.domain.entity.ComponenteEvaluacion;
import edu.sia.evaluacion.domain.repository.ComponenteEvaluacionRepository;
import edu.sia.exception.BusinessException;
import edu.sia.exception.ResourceNotFoundException;
import edu.sia.grupo.domain.repository.GrupoRepository;
import edu.sia.matricula.domain.entity.Matricula;
import edu.sia.matricula.domain.repository.MatriculaRepository;
import edu.sia.nota.application.dto.*;
import edu.sia.nota.domain.entity.HistorialNota;
import edu.sia.nota.domain.entity.Nota;
import edu.sia.nota.domain.repository.HistorialNotaRepository;
import edu.sia.nota.domain.repository.NotaRepository;
import edu.sia.users.domain.entity.User;
import edu.sia.users.domain.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;

@Service
@RequiredArgsConstructor
@Slf4j
public class NotaService {

    private final NotaRepository notaRepository;
    private final MatriculaRepository matriculaRepository;
    private final ComponenteEvaluacionRepository componenteRepository;
    private final GrupoRepository grupoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final IUserRepository userRepository;
    private final HistorialNotaRepository historialNotaRepository;

    @Transactional
    public Mono<NotaDto> createNota(Long grupoId, CreateNotaDto dto) {
        log.info("Creating nota for grupo: {}, estudiante: {}, componente: {}",
                grupoId, dto.getEstudianteId(), dto.getComponenteId());

        return grupoRepository.findById(grupoId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Grupo no encontrado con ID: " + grupoId)))
                .flatMap(grupo -> matriculaRepository.findByEstudianteIdAndGrupoId(
                        dto.getEstudianteId(), grupoId)
                        .switchIfEmpty(Mono.error(new BusinessException(
                                "El estudiante no está matriculado en este grupo")))
                        .flatMap(matricula -> componenteRepository.findById(dto.getComponenteId())
                                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                                        "Componente de evaluación no encontrado con ID: " + dto.getComponenteId())))
                                .flatMap(componente -> {
                                    if (!componente.getAsignaturaId().equals(grupo.getAsignaturaId())) {
                                        return Mono.error(new BusinessException(
                                                "El componente no pertenece a la asignatura del grupo"));
                                    }

                                    return notaRepository.findByMatriculaIdAndComponenteId(
                                            matricula.getMatriculaId(), dto.getComponenteId())
                                            .flatMap(existingNota -> Mono.<Nota>error(
                                                    new BusinessException("Ya existe una nota para este componente")))
                                            .switchIfEmpty(Mono.defer(() -> {
                                                Nota nota = Nota.builder()
                                                        .matriculaId(matricula.getMatriculaId())
                                                        .componenteId(dto.getComponenteId())
                                                        .valor(dto.getValor())
                                                        .fechaRegistro(LocalDateTime.now())
                                                        .build();

                                                return notaRepository.save(nota);
                                            }))
                                            .flatMap(savedNota -> buildNotaDto(savedNota, matricula, componente));
                                })));
    }

    @Transactional
    public Mono<NotaDto> updateNota(Long notaId, UpdateNotaDto dto, Long modificadoPor) {
        log.info("Updating nota: {}", notaId);

        return notaRepository.findById(notaId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Nota no encontrada con ID: " + notaId)))
                .flatMap(nota -> {
                    BigDecimal valorAnterior = nota.getValor();

                    // Crear historial
                    HistorialNota historial = HistorialNota.builder()
                            .notaId(notaId)
                            .valorAnterior(valorAnterior)
                            .valorNuevo(dto.getValor())
                            .justificacion(dto.getJustificacion())
                            .modificadoPor(modificadoPor)
                            .fechaModificacion(LocalDateTime.now())
                            .build();

                    // Actualizar nota
                    nota.setValor(dto.getValor());
                    nota.setJustificacion(dto.getJustificacion());
                    nota.setFechaModificacion(LocalDateTime.now());
                    nota.setModificadoPor(modificadoPor);

                    return historialNotaRepository.save(historial)
                            .then(notaRepository.save(nota))
                            .flatMap(savedNota -> matriculaRepository.findById(savedNota.getMatriculaId())
                                    .flatMap(matricula -> componenteRepository.findById(savedNota.getComponenteId())
                                            .flatMap(componente -> buildNotaDto(savedNota, matricula, componente))));
                });
    }

    public Flux<CalificacionPorAsignaturaDto> getCalificacionesByEstudiante(Long estudianteId) {
        log.info("Getting calificaciones for estudiante: {}", estudianteId);

        return userRepository.findById(estudianteId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Estudiante no encontrado con ID: " + estudianteId)))
                .flatMapMany(estudiante -> matriculaRepository.findActiveByEstudianteId(estudianteId)
                        .flatMap(matricula -> grupoRepository.findById(matricula.getGrupoId())
                                .flatMap(grupo -> asignaturaRepository.findById(grupo.getAsignaturaId())
                                        .flatMap(asignatura -> calcularNotaFinal(matricula.getMatriculaId())
                                                .map(notaFinal -> CalificacionPorAsignaturaDto.builder()
                                                        .asignaturaId(asignatura.getAsignaturaId())
                                                        .asignaturaNombre(asignatura.getNombre())
                                                        .asignaturaCodigo(asignatura.getCodigo())
                                                        .grupoId(grupo.getGrupoId())
                                                        .periodo(grupo.getPeriodo())
                                                        .notaFinal(notaFinal)
                                                        .estado(matricula.getEstado())
                                                        .build())))));
    }

    public Mono<DetalleCalificacionDto> getDetalleCalificacion(Long estudianteId, Long asignaturaId) {
        log.info("Getting detalle calificacion for estudiante: {}, asignatura: {}",
                estudianteId, asignaturaId);

        return asignaturaRepository.findById(asignaturaId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Asignatura no encontrada con ID: " + asignaturaId)))
                .flatMap(asignatura -> notaRepository.findByEstudianteIdAndAsignaturaId(estudianteId, asignaturaId)
                        .flatMap(nota -> componenteRepository.findById(nota.getComponenteId())
                                .map(componente -> {
                                    BigDecimal valorPonderado = nota.getValor()
                                            .multiply(componente.getPorcentaje())
                                            .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP);

                                    return NotaPorComponenteDto.builder()
                                            .componenteId(componente.getComponenteId())
                                            .componenteNombre(componente.getNombre())
                                            .porcentaje(componente.getPorcentaje())
                                            .valor(nota.getValor())
                                            .valorPonderado(valorPonderado)
                                            .build();
                                }))
                        .collectList()
                        .flatMap(componentes -> {
                            BigDecimal notaFinal = componentes.stream()
                                    .map(NotaPorComponenteDto::getValorPonderado)
                                    .reduce(BigDecimal.ZERO, BigDecimal::add);

                            return matriculaRepository.findByEstudianteId(estudianteId)
                                    .next()
                                    .flatMap(matricula -> grupoRepository.findById(matricula.getGrupoId())
                                            .map(grupo -> DetalleCalificacionDto.builder()
                                                    .asignaturaId(asignatura.getAsignaturaId())
                                                    .asignaturaNombre(asignatura.getNombre())
                                                    .asignaturaCodigo(asignatura.getCodigo())
                                                    .periodo(grupo.getPeriodo())
                                                    .componentes(componentes)
                                                    .notaFinal(notaFinal)
                                                    .build()));
                        }));
    }

    private Mono<BigDecimal> calcularNotaFinal(Long matriculaId) {
        return notaRepository.findByMatriculaId(matriculaId)
                .flatMap(nota -> componenteRepository.findById(nota.getComponenteId())
                        .map(componente -> nota.getValor()
                                .multiply(componente.getPorcentaje())
                                .divide(BigDecimal.valueOf(100), 2, RoundingMode.HALF_UP)))
                .reduce(BigDecimal.ZERO, BigDecimal::add);
    }

    private Mono<NotaDto> buildNotaDto(Nota nota, Matricula matricula, ComponenteEvaluacion componente) {
        Mono<User> estudianteMono = userRepository.findById(matricula.getEstudianteId());
        Mono<User> modificadoPorMono = nota.getModificadoPor() != null
                ? userRepository.findById(nota.getModificadoPor())
                : Mono.empty();

        return Mono.zip(estudianteMono, modificadoPorMono.defaultIfEmpty(new User()))
                .map(tuple -> {
                    User estudiante = tuple.getT1();
                    User modificadoPor = tuple.getT2().getUsuarioId() != null ? tuple.getT2() : null;

                    return NotaDto.builder()
                            .notaId(nota.getNotaId())
                            .matriculaId(nota.getMatriculaId())
                            .estudianteId(estudiante.getUsuarioId())
                            .estudianteNombre(estudiante.getGivenName() + " " + estudiante.getFamilyName())
                            .componenteId(componente.getComponenteId())
                            .componenteNombre(componente.getNombre())
                            .componentePorcentaje(componente.getPorcentaje())
                            .valor(nota.getValor())
                            .justificacion(nota.getJustificacion())
                            .fechaRegistro(nota.getFechaRegistro())
                            .fechaModificacion(nota.getFechaModificacion())
                            .modificadoPor(nota.getModificadoPor())
                            .modificadoPorNombre(modificadoPor != null ?
                                    modificadoPor.getGivenName() + " " + modificadoPor.getFamilyName() : null)
                            .build();
                });
    }
}


