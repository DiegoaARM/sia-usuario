package edu.sia.tenant.domain.repository;

import edu.sia.tenant.domain.entity.Tenant;
import org.springframework.data.repository.reactive.ReactiveCrudRepository;
import org.springframework.stereotype.Repository;
import reactor.core.publisher.Mono;

@Repository
public interface TenantRepository extends ReactiveCrudRepository<Tenant, Long> {
    Mono<Tenant> findByDomain(String domain);
    Mono<Tenant> findByEmail(String email);
}
