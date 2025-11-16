package pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

import java.util.List;
import java.util.UUID;

@Value
@Builder
public class RoleAssignmentResponse {
    UUID id;
    String name;
    String email;
    RoleName role;
    List<RoleName> availableRoles;
}
