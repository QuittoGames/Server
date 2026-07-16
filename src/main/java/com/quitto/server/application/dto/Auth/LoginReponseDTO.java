package com.quitto.server.application.dto.Auth;

import java.util.Date;

public record LoginReponseDTO(
    String token,
    Date date
) {

}
