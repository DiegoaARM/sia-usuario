package edu.sia.tenant.application.service;

import edu.sia.tenant.application.dto.CreateTenantDto;
import edu.sia.tenant.domain.entity.Tenant;

import java.util.List;
import java.util.Optional;

public interface ITenantService {
    List<Tenant> findAll();
    Optional<Tenant> findById(Long id);
    Tenant create(CreateTenantDto dto);
    Tenant update(Long id, Tenant tenant);
    void delete(Long id);
}