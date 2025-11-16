package pe.com.ecocleany.ecosmart.operations.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;
import pe.com.ecocleany.ecosmart.operations.domain.model.CollectionRouteCheckpoint.CheckpointStatus;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class RouteCheckpointResponse {
    UUID smartBinId;
    CheckpointStatus status;
    Instant visitedAt;
    UUID confirmedBy;
}
