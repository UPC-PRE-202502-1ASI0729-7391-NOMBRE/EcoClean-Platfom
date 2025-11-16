package pe.com.ecocleany.ecosmart.operations.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;
import pe.com.ecocleany.ecosmart.operations.domain.model.CollectionRoute.RouteStatus;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Value
@Builder
public class CollectionRouteResponse {
    UUID id;
    String name;
    UUID municipalityId;
    List<UUID> smartBinIds;
    Instant scheduledAt;
    RouteStatus status;
    UUID operatorId;
    List<RouteCheckpointResponse> checkpoints;
}
