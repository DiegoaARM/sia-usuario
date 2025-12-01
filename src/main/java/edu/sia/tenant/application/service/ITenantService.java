package edu.sia.tenant.application.service;

import edu.sia.tenant.application.dto.CreateTenantDto;
import edu.sia.tenant.domain.entity.Tenant;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface ITenantService {
    Flux<Tenant> findAll();
    Mono<Tenant> findById(Long id);
    Mono<Tenant> create(CreateTenantDto dto);
    Mono<Tenant> update(Long id, Tenant tenant);
    Mono<Void> delete(Long id);
}