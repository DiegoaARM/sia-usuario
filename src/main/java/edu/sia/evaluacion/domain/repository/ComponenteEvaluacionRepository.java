package edu.sia.evaluacion.domain.repository;

import edu.sia.evaluacion.domain.entity.ComponenteEvaluacion;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.math.BigDecimal;

@Repository
public interface ComponenteEvaluacionRepository extends ReactiveCrudRepository<ComponenteEvaluacion, Long> {

    Flux<ComponenteEvaluacion> findByAsignaturaId(Long asignaturaId);

    @Query("SELECT * FROM componentes_evaluacion WHERE asignatura_id = :asignaturaId AND estado = 'ACTIVE'")
    Flux<ComponenteEvaluacion> findActiveByAsignaturaId(Long asignaturaId);

    @Query("SELECT COALESCE(SUM(porcentaje), 0) FROM componentes_evaluacion WHERE asignatura_id = :asignaturaId AND estado = 'ACTIVE'")
    Mono<BigDecimal> sumPorcentajesByAsignaturaId(Long asignaturaId);

    @Query("SELECT EXISTS(SELECT 1 FROM componentes_evaluacion WHERE asignatura_id = :asignaturaId AND nombre = :nombre AND componente_id != :componenteId)")
    Mono<Boolean> existsByAsignaturaIdAndNombreAndNotId(Long asignaturaId, String nombre, Long componenteId);
}

