package edu.sia.grupo.domain.repository;

import edu.sia.grupo.domain.entity.Grupo;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface GrupoRepository extends ReactiveCrudRepository<Grupo, Long> {

    Flux<Grupo> findByAsignaturaId(Long asignaturaId);

    Flux<Grupo> findByDocenteId(Long docenteId);

    Flux<Grupo> findByPeriodo(String periodo);

    @Query("SELECT * FROM grupos WHERE docente_id = :docenteId AND estado = 'ACTIVE'")
    Flux<Grupo> findActiveByDocenteId(Long docenteId);

    @Query("SELECT COUNT(*) FROM matriculas WHERE grupo_id = :grupoId AND estado = 'ACTIVE'")
    Mono<Long> countMatriculadosByGrupoId(Long grupoId);
}

