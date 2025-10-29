package edu.sia.role;

import com.fasterxml.jackson.annotation.JsonCreator;
import lombok.Getter;

import java.util.Arrays;
import java.util.Set;

@Getter
public enum Role {

        SYSTEM_USER_ADMINISTRATOR("Administrador de usuario de una universidad", Set.of(
            Permission.CREATE_STUDENT,
            Permission.CREATE_TEACHER
    )),

    SIA_TENANT_MANAGER("Administrador de tenants del sistema, puede crear universidades", Set.of(
            Permission.CREATE_TENANT
    )),

    STUDENT("Estudiante de una universidad", Set.of(
            // Permisos específicos para el rol de estudiante
    )),

    TEACHER("Profesor de una universidad", Set.of(
            // Permisos específicos para el rol de profesor
    ));


    private final String description;
    private final Set<Permission> permissions;

    Role(String description, Set<Permission> permissions) {
        this.description = description;
        this.permissions = permissions;
    }

    @JsonCreator
    public static Role fromValue(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El rol no puede ser nulo o vacío");
        }

        try {
            return Role.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            String allowed = Arrays.toString(Role.values());
            throw new IllegalArgumentException(
                    String.format("El rol '%s' no es válido. Valores permitidos: %s", value, allowed)
            );
        }
    }
}
