package pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

public record ModifyRoleRequest(
        @NotNull(message = "El rol es obligatorio")
        RoleName role
) {
}
