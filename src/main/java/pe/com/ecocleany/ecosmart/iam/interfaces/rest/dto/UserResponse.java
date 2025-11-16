package pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

import java.util.Set;
import java.util.UUID;

@Value
@Builder
public class UserResponse {
    UUID id;
    String email;
    String firstName;
    String paternalLastName;
    String maternalLastName;
    String dni;
    String phoneNumber;
    String department;
    String province;
    String district;
    UUID municipalityId;
    Set<RoleName> roles;
}
