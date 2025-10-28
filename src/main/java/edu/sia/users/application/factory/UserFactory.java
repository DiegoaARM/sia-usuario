package edu.sia.users.application.factory;

import edu.sia.tenant.domain.entity.Tenant;
import edu.sia.users.application.dto.CreateUserDto;
import edu.sia.users.domain.entity.User;
import org.springframework.stereotype.Component;

@Component
public class UserFactory {

    /**
     * Creates a User entity from a CreateUserDto using the builder pattern.
     *
     * @param dto the DTO containing user creation data
     * @param tenant the tenant entity to associate with the user
     * @return a new User entity
     */
    public User createFromDto(String userId, Tenant tenant, CreateUserDto dto) {
        return User.builder()
                .email(dto.email())
                .givenName(dto.givenName())
                .familyName(dto.familyName())
                .idType(dto.idType())
                .idNumber(dto.idNumber())
                .roleName(dto.roleName())
                .phoneNumber(dto.phoneNumber())
                .birthdate(dto.birthdate())
                .genre(dto.genre())
                .state("ACTIVE")
                .tenantId(tenant)
                .build();
    }
}
