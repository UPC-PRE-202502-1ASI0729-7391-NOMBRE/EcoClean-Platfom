package pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto;

import jakarta.validation.constraints.NotNull;

import java.util.UUID;

public record UpdateUserMunicipalityRequest(
        @NotNull(message = "El municipio es obligatorio")
        UUID municipalityId
) {
}
