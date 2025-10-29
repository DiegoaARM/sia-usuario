package edu.sia.role;

import com.fasterxml.jackson.annotation.JsonCreator;

import java.util.Arrays;

public enum Permission {

    // USUARIOS
    CREATE_STUDENT,
    CREATE_TEACHER,

    // TENANTS
    CREATE_TENANT;

    @JsonCreator
    public static Permission fromValue(String value) throws  IllegalArgumentException {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("El permiso no puede ser nulo o vacío");
        }
        try {
            return Permission.valueOf(value.trim().toUpperCase());
        } catch (IllegalArgumentException ex) {
            String allowed = Arrays.toString(Permission.values());
            throw new IllegalArgumentException(
                    String.format("El permiso '%s' no es válido. Valores permitidos: %s", value, allowed)
            );
        }
    }
}
