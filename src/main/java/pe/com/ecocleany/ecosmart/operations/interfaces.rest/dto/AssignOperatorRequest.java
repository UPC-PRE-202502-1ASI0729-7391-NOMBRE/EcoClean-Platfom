package pe.com.ecocleany.ecosmart.operations.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record AssignOperatorRequest(
        @NotNull(message = "El operador es obligatorio")
        UUID operatorId
) {
}
