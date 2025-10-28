package edu.sia.service.impl;

import edu.sia.model.Tenant;
import edu.sia.repository.TenantRepository;
import edu.sia.service.TenantService;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class TenantServiceImpl implements TenantService {

    private final TenantRepository tenantRepository;

    public TenantServiceImpl(TenantRepository tenantRepository) {
        this.tenantRepository = tenantRepository;
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
    public Tenant save(Tenant tenant) {
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
