package edu.sia.nota.domain.repository;

import edu.sia.nota.domain.entity.Nota;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface NotaRepository extends ReactiveCrudRepository<Nota, Long> {

    Flux<Nota> findByMatriculaId(Long matriculaId);

    Mono<Nota> findByMatriculaIdAndComponenteId(Long matriculaId, Long componenteId);

    @Query("SELECT n.* FROM notas n " +
           "INNER JOIN matriculas m ON n.matricula_id = m.matricula_id " +
           "WHERE m.estudiante_id = :estudianteId")
    Flux<Nota> findByEstudianteId(Long estudianteId);

    @Query("SELECT n.* FROM notas n " +
           "INNER JOIN matriculas m ON n.matricula_id = m.matricula_id " +
           "INNER JOIN grupos g ON m.grupo_id = g.grupo_id " +
           "WHERE m.estudiante_id = :estudianteId " +
           "AND g.asignatura_id = :asignaturaId")
    Flux<Nota> findByEstudianteIdAndAsignaturaId(Long estudianteId, Long asignaturaId);

    @Query("SELECT n.* FROM notas n " +
           "INNER JOIN matriculas m ON n.matricula_id = m.matricula_id " +
           "WHERE m.grupo_id = :grupoId")
    Flux<Nota> findByGrupoId(Long grupoId);
}

