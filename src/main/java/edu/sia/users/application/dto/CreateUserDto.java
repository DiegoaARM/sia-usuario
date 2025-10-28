package edu.sia.users.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Past;

import java.time.LocalDate;

public record CreateUserDto(
        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe ser válido")
        String email,

        @NotBlank(message = "El nombre es obligatorio")
        String givenName,

        @NotBlank(message = "El apellido es obligatorio")
        String familyName,

        @NotBlank(message = "El tipo de documento es obligatorio")
        String idType,

        @NotBlank(message = "El número de documento es obligatorio")
        String idNumber,

        @NotBlank(message = "El rol es obligatorio")
        String roleName,

        @NotBlank(message = "El número de teléfono es obligatorio")
        String phoneNumber,

        @NotNull(message = "La fecha de nacimiento es obligatoria")
        @Past(message = "La fecha de nacimiento debe ser en el pasado")
        LocalDate birthdate,

        @NotBlank(message = "El género es obligatorio")
        String genre,

        @NotNull(message = "El tenant ID es obligatorio")
        Long tenantId
) {
}
