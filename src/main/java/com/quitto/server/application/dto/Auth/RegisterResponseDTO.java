package com.quitto.server.application.dto.Auth;

import java.util.Date;

public record RegisterResponseDTO(
    String Token,
    Date date
) {

}
