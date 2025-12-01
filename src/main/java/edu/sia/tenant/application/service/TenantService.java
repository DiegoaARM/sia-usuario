package edu.sia.tenant.application.service;

import edu.sia.tenant.application.dto.CreateTenantDto;
import edu.sia.tenant.application.factory.TenantFactory;
import edu.sia.tenant.domain.entity.Tenant;
import edu.sia.tenant.domain.repository.TenantRepository;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class TenantService implements ITenantService {

    private final TenantRepository tenantRepository;
    private final TenantFactory tenantFactory;

    public TenantService(TenantRepository tenantRepository, TenantFactory tenantFactory) {
        this.tenantRepository = tenantRepository;
        this.tenantFactory = tenantFactory;
    }

    @Override
    public Flux<Tenant> findAll() {
        return tenantRepository.findAll();
    }

    @Override
    public Mono<Tenant> findById(Long id) {
        return tenantRepository.findById(id);
    }

    @Override
    public Mono<Tenant> create(CreateTenantDto dto) {
        var tenant = tenantFactory.createFromDto(dto);
        return tenantRepository.save(tenant);
    }

    @Override
    public Mono<Tenant> update(Long id, Tenant tenant) {
        return tenantRepository.findById(id)
                .switchIfEmpty(Mono.error(new RuntimeException("Tenant no encontrado con id: " + id)))
                .flatMap(existing -> {
                    tenant.setTenantId(existing.getTenantId());
                    return tenantRepository.save(tenant);
                });
    }

    @Override
    public Mono<Void> delete(Long id) {
        return tenantRepository.deleteById(id);
    }
}
