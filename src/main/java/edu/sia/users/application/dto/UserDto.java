package edu.sia.users.application.dto;

public record UserDto(
    Long usuarioId,
    String email,
    String givenName,
    String familyName,
    String idType,
    String idNumber,
    String roleName,
    String phoneNumber,
    String birthdate,
    String genre,
    String state,
    String lastLogin,
    String createdAt
) {


}
