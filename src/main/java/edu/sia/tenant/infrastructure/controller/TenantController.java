package edu.sia.tenant.infrastructure.controller;

import edu.sia.tenant.application.dto.CreateTenantDto;
import edu.sia.tenant.domain.entity.Tenant;
import edu.sia.tenant.application.service.ITenantService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/tenants")
@CrossOrigin(origins = "http://localhost:5173")
public class TenantController {

    private final ITenantService ITenantService;

    public TenantController(ITenantService ITenantService) {
        this.ITenantService = ITenantService;
    }

    @GetMapping
    public ResponseEntity<List<Tenant>> getAll() {
        return ResponseEntity.ok(ITenantService.findAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Tenant> getById(@PathVariable Long id) {
        return ITenantService.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<Tenant> create(@RequestBody CreateTenantDto dto) {
        return ResponseEntity.ok(ITenantService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<Tenant> update(@PathVariable Long id, @RequestBody Tenant tenant) {
        return ResponseEntity.ok(ITenantService.update(id, tenant));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        ITenantService.delete(id);
        return ResponseEntity.noContent().build();
    }
}
