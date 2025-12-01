package edu.sia.matricula.domain.repository;

import edu.sia.matricula.domain.entity.Matricula;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface MatriculaRepository extends ReactiveCrudRepository<Matricula, Long> {

    Flux<Matricula> findByEstudianteId(Long estudianteId);

    Flux<Matricula> findByGrupoId(Long grupoId);

    @Query("SELECT * FROM matriculas WHERE estudiante_id = :estudianteId AND estado = 'ACTIVE'")
    Flux<Matricula> findActiveByEstudianteId(Long estudianteId);

    @Query("SELECT * FROM matriculas WHERE grupo_id = :grupoId AND estado = 'ACTIVE'")
    Flux<Matricula> findActiveByGrupoId(Long grupoId);

    Mono<Matricula> findByEstudianteIdAndGrupoId(Long estudianteId, Long grupoId);

    @Query("SELECT COUNT(*) FROM matriculas WHERE grupo_id = :grupoId AND estado = 'ACTIVE'")
    Mono<Long> countActiveByGrupoId(Long grupoId);

    @Query("SELECT m.* FROM matriculas m " +
           "INNER JOIN grupos g ON m.grupo_id = g.grupo_id " +
           "WHERE m.estudiante_id = :estudianteId " +
           "AND g.periodo = :periodo " +
           "AND m.estado = 'ACTIVE'")
    Flux<Matricula> findByEstudianteIdAndPeriodo(Long estudianteId, String periodo);
}

