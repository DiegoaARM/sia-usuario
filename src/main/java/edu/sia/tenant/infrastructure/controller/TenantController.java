package edu.sia.tenant.infrastructure.controller;

import edu.sia.tenant.application.dto.CreateTenantDto;
import edu.sia.tenant.domain.entity.Tenant;
import edu.sia.tenant.application.service.ITenantService;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/tenants")
@CrossOrigin(origins = "http://localhost:5173")
public class TenantController {

    private final ITenantService ITenantService;

    public TenantController(ITenantService ITenantService) {
        this.ITenantService = ITenantService;
    }

    @GetMapping
    public Flux<Tenant> getAll() {
        return ITenantService.findAll();
    }

    @GetMapping("/{id}")
    public Mono<Tenant> getById(@PathVariable Long id) {
        return ITenantService.findById(id);
    }

    @PostMapping
    @PreAuthorize("hasRole(\"SIA_TENANT_MANAGER\")")
    public Mono<Tenant> create(@RequestBody CreateTenantDto dto) {
        return ITenantService.create(dto);
    }

    @PutMapping("/{id}")
    public Mono<Tenant> update(@PathVariable Long id, @RequestBody Tenant tenant) {
        return ITenantService.update(id, tenant);
    }

    @DeleteMapping("/{id}")
    public Mono<Void> delete(@PathVariable Long id) {
        return ITenantService.delete(id);
    }
}
