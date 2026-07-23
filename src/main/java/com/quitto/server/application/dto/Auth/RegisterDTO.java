package com.quitto.server.application.dto.Auth;

public record RegisterDTO(
    String name,
    String password,
    String email
) {

}
