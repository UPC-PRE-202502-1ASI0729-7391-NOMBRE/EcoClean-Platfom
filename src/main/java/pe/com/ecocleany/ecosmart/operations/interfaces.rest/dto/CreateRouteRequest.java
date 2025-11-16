package pe.com.ecocleany.ecosmart.operations.interfaces.rest.dto;

import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Data
public class CreateRouteRequest {

    @NotBlank(message = "El nombre de la ruta es obligatorio")
    private String name;

    @NotNull(message = "El municipio es obligatorio")
    private UUID municipalityId;

    @NotEmpty(message = "Debe seleccionar al menos un SmartBin")
    private List<UUID> smartBinIds;

    @Future(message = "La fecha programada debe ser futura")
    private Instant scheduledAt;
}
