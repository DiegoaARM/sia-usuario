package edu.sia.asignatura.domain.repository;

import edu.sia.asignatura.domain.entity.Asignatura;
import org.springframework.data.r2dbc.repository.Query;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Repository
public interface AsignaturaRepository extends ReactiveCrudRepository<Asignatura, Long> {

    Mono<Asignatura> findByCodigo(String codigo);

    Flux<Asignatura> findByTenantId(Long tenantId);

    Mono<Boolean> existsByCodigo(String codigo);

    @Query("SELECT * FROM asignaturas WHERE tenant_id = :tenantId AND estado = 'ACTIVE'")
    Flux<Asignatura> findActiveByTenantId(Long tenantId);
}


