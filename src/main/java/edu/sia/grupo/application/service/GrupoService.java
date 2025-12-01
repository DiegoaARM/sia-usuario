package edu.sia.grupo.application.service;

import edu.sia.asignatura.domain.entity.Asignatura;
import edu.sia.asignatura.domain.repository.AsignaturaRepository;
import edu.sia.exception.BusinessException;
import edu.sia.exception.ResourceNotFoundException;
import edu.sia.grupo.application.dto.*;
import edu.sia.grupo.domain.entity.Grupo;
import edu.sia.grupo.domain.repository.GrupoRepository;
import edu.sia.matricula.domain.repository.MatriculaRepository;
import edu.sia.users.domain.entity.User;
import edu.sia.users.domain.repository.IUserRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
@RequiredArgsConstructor
@Slf4j
public class GrupoService {

    private final GrupoRepository grupoRepository;
    private final AsignaturaRepository asignaturaRepository;
    private final IUserRepository userRepository;
    private final MatriculaRepository matriculaRepository;

    @Transactional
    public Mono<GrupoDto> createGrupo(CreateGrupoDto dto) {
        log.info("Creating grupo for asignatura: {}", dto.getAsignaturaId());

        return asignaturaRepository.findById(dto.getAsignaturaId())
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Asignatura no encontrada con ID: " + dto.getAsignaturaId())))
                .flatMap(asignatura -> {
                    Grupo grupo = Grupo.builder()
                            .asignaturaId(dto.getAsignaturaId())
                            .periodo(dto.getPeriodo())
                            .cupo(dto.getCupo())
                            .horario(dto.getHorario())
                            .estado("ACTIVE")
                            .build();

                    return grupoRepository.save(grupo)
                            .flatMap(savedGrupo -> buildGrupoDto(savedGrupo, asignatura, null));
                });
    }

    @Transactional
    public Mono<GrupoDto> asignarDocente(Long grupoId, AsignarDocenteDto dto) {
        log.info("Assigning docente {} to grupo {}", dto.getDocenteId(), grupoId);

        return grupoRepository.findById(grupoId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Grupo no encontrado con ID: " + grupoId)))
                .flatMap(grupo -> userRepository.findById(dto.getDocenteId())
                        .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                                "Docente no encontrado con ID: " + dto.getDocenteId())))
                        .flatMap(docente -> {
                            if (!"DOCENTE".equalsIgnoreCase(docente.getRoleName())) {
                                return Mono.error(new BusinessException(
                                        "El usuario no tiene rol de docente"));
                            }

                            grupo.setDocenteId(dto.getDocenteId());
                            return grupoRepository.save(grupo);
                        })
                        .flatMap(savedGrupo -> asignaturaRepository.findById(savedGrupo.getAsignaturaId())
                                .flatMap(asignatura -> userRepository.findById(savedGrupo.getDocenteId())
                                        .flatMap(docente -> buildGrupoDto(savedGrupo, asignatura, docente)))));
    }

    public Mono<GrupoDto> getGrupoById(Long grupoId) {
        return grupoRepository.findById(grupoId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Grupo no encontrado con ID: " + grupoId)))
                .flatMap(grupo -> {
                    Mono<Asignatura> asignaturaMono = asignaturaRepository.findById(grupo.getAsignaturaId());
                    Mono<User> docenteMono = grupo.getDocenteId() != null
                            ? userRepository.findById(grupo.getDocenteId())
                            : Mono.empty();

                    return Mono.zip(asignaturaMono, docenteMono.defaultIfEmpty(new User()))
                            .flatMap(tuple -> buildGrupoDto(grupo, tuple.getT1(),
                                    tuple.getT2().getUsuarioId() != null ? tuple.getT2() : null));
                });
    }

    public Flux<GrupoConEstudiantesDto> getGruposByDocenteId(Long docenteId) {
        log.info("Getting grupos for docente: {}", docenteId);

        return userRepository.findById(docenteId)
                .switchIfEmpty(Mono.error(new ResourceNotFoundException(
                        "Docente no encontrado con ID: " + docenteId)))
                .flatMapMany(docente -> {
                    if (!"DOCENTE".equalsIgnoreCase(docente.getRoleName())) {
                        return Flux.error(new BusinessException(
                                "El usuario no tiene rol de docente"));
                    }

                    return grupoRepository.findActiveByDocenteId(docenteId)
                            .flatMap(this::buildGrupoConEstudiantesDto);
                });
    }

    private Mono<GrupoDto> buildGrupoDto(Grupo grupo, Asignatura asignatura, User docente) {
        return matriculaRepository.countActiveByGrupoId(grupo.getGrupoId())
                .map(count -> GrupoDto.builder()
                        .grupoId(grupo.getGrupoId())
                        .asignaturaId(asignatura.getAsignaturaId())
                        .asignaturaNombre(asignatura.getNombre())
                        .asignaturaCodigo(asignatura.getCodigo())
                        .docenteId(grupo.getDocenteId())
                        .docenteNombre(docente != null ?
                                docente.getGivenName() + " " + docente.getFamilyName() : null)
                        .periodo(grupo.getPeriodo())
                        .cupo(grupo.getCupo())
                        .matriculados(count.intValue())
                        .horario(grupo.getHorario())
                        .estado(grupo.getEstado())
                        .createdAt(grupo.getCreatedAt())
                        .build());
    }

    private Mono<GrupoConEstudiantesDto> buildGrupoConEstudiantesDto(Grupo grupo) {
        return asignaturaRepository.findById(grupo.getAsignaturaId())
                .flatMap(asignatura -> matriculaRepository.findActiveByGrupoId(grupo.getGrupoId())
                        .flatMap(matricula -> userRepository.findById(matricula.getEstudianteId())
                                .map(estudiante -> EstudianteMatriculadoDto.builder()
                                        .estudianteId(estudiante.getUsuarioId())
                                        .nombreCompleto(estudiante.getGivenName() + " " +
                                                estudiante.getFamilyName())
                                        .email(estudiante.getEmail())
                                        .idNumber(estudiante.getIdNumber())
                                        .fechaMatricula(matricula.getFechaMatricula())
                                        .estado(matricula.getEstado())
                                        .build()))
                        .collectList()
                        .map(estudiantes -> GrupoConEstudiantesDto.builder()
                                .grupoId(grupo.getGrupoId())
                                .asignaturaNombre(asignatura.getNombre())
                                .asignaturaCodigo(asignatura.getCodigo())
                                .periodo(grupo.getPeriodo())
                                .cupo(grupo.getCupo())
                                .matriculados(estudiantes.size())
                                .horario(grupo.getHorario())
                                .estudiantes(estudiantes)
                                .build()));
    }
}

