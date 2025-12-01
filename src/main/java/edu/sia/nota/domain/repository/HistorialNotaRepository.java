package edu.sia.nota.domain.repository;

import edu.sia.nota.domain.entity.HistorialNota;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Flux;

@Repository
public interface HistorialNotaRepository extends ReactiveCrudRepository<HistorialNota, Long> {

    Flux<HistorialNota> findByNotaId(Long notaId);

    Flux<HistorialNota> findByNotaIdOrderByFechaModificacionDesc(Long notaId);
}

