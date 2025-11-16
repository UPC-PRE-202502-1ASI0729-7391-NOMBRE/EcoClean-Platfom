package pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;

import java.time.Instant;

@Value
@Builder
public class AuthResponse {
    String token;
    Instant expiresAt;
    UserResponse user;
}
