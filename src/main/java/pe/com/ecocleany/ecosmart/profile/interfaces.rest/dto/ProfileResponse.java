package pe.com.ecocleany.ecosmart.profile.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.UserResponse;

import java.time.Instant;

@Value
@Builder
public class ProfileResponse {
    UserResponse user;
    String avatarUrl;
    String bio;
    String occupation;
    String addressReference;
    String emergencyContact;
    String emergencyPhone;
    Instant updatedAt;
}
