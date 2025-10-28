package edu.sia.tenant.application.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

public record CreateTenantDto(
        @NotBlank(message = "El nombre del tenant es obligatorio")
        String name,

        @NotBlank(message = "El dominio es obligatorio")
        String domain,

        @NotBlank(message = "El email es obligatorio")
        @Email(message = "El email debe ser válido")
        String email,

        @NotBlank(message = "El número de teléfono es obligatorio")
        String phoneNumber,

        @NotBlank(message = "El país es obligatorio")
        String country,

        @NotBlank(message = "El idioma es obligatorio")
        String language,

        @NotBlank(message = "La zona horaria es obligatoria")
        String timezone
) {
}
