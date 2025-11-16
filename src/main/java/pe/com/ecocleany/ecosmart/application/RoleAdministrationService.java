package pe.com.ecocleany.ecosmart.admin.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.iam.application.RoleManagementService;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.UserResponse;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class RoleAdministrationService {

    private final RoleManagementService roleManagementService;

    public UserResponse assignRole(UUID userId, RoleName roleName) {
        return roleManagementService.assignRole(userId, roleName);
    }

    public UserResponse removeRole(UUID userId, RoleName roleName) {
        return roleManagementService.removeRole(userId, roleName);
    }
}
