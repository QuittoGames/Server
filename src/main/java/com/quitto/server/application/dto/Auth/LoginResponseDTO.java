package com.quitto.server.application.dto.Auth;

import java.util.Date;

public record LoginResponseDTO(
    String token,
    Date date
) {

}
