package com.quitto.server.application.dto.Auth;

import java.util.Date;

public record RegisterResponseDTO(
    String token,
    Date date
) {

}
