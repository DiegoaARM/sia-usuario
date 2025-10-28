package edu.sia.tenant.application.factory;

import edu.sia.tenant.application.dto.CreateTenantDto;
import edu.sia.tenant.domain.entity.Tenant;
import org.springframework.stereotype.Component;

@Component
public class TenantFactory {

    /**
     * Creates a Tenant entity from a CreateTenantDto using the builder pattern.
     *
     * @param dto the DTO containing tenant creation data
     * @return a new Tenant entity
     */
    public Tenant createFromDto(CreateTenantDto dto) {
        return Tenant.builder()
                .name(dto.name())
                .domain(dto.domain())
                .email(dto.email())
                .phoneNumber(dto.phoneNumber())
                .country(dto.country())
                .language(dto.language())
                .timezone(dto.timezone())
                .state("ACTIVE")
                .build();
    }
}

