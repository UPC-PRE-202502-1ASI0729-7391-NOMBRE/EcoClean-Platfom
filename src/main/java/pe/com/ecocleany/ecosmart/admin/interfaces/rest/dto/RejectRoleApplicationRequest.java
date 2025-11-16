package pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;

public record RejectRoleApplicationRequest(
        @NotBlank(message = "El motivo de rechazo es obligatorio")
        String reason
) {
}
