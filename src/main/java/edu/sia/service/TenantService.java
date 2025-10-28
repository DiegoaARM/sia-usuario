package edu.sia.service;

import edu.sia.model.Tenant;

import java.util.List;
import java.util.Optional;

public interface TenantService {
    List<Tenant> findAll();
    Optional<Tenant> findById(Long id);
    Tenant save(Tenant tenant);
    Tenant update(Long id, Tenant tenant);
    void delete(Long id);
}