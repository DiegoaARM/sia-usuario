package edu.sia.tenant.application.service;

import edu.sia.tenant.application.dto.CreateTenantDto;
import edu.sia.tenant.application.factory.TenantFactory;
import edu.sia.tenant.domain.entity.Tenant;
import edu.sia.tenant.domain.repository.TenantRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantService implements ITenantService {

    private final TenantRepository tenantRepository;
    private final TenantFactory tenantFactory;

    public TenantService(TenantRepository tenantRepository, TenantFactory tenantFactory) {
        this.tenantRepository = tenantRepository;
        this.tenantFactory = tenantFactory;
    }

    @Override
    public List<Tenant> findAll() {
        return tenantRepository.findAll();
    }

    @Override
    public Optional<Tenant> findById(Long id) {
        return tenantRepository.findById(id);
    }

    @Override
    public Tenant create(CreateTenantDto dto) {
        var tenant = tenantFactory.createFromDto(dto);
        return tenantRepository.save(tenant);
    }

    @Override
    public Tenant update(Long id, Tenant tenant) {
        return tenantRepository.findById(id)
                .map(existing -> {
                    tenant.setTenantId(existing.getTenantId());
                    return tenantRepository.save(tenant);
                })
                .orElseThrow(() -> new RuntimeException("Tenant no encontrado con id: " + id));
    }

    @Override
    public void delete(Long id) {
        tenantRepository.deleteById(id);
    }
}
