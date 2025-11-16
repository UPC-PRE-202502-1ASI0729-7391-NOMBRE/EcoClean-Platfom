package pe.com.ecocleany.ecosmart.operations.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.operations.domain.model.CollectionRouteCheckpoint;
import pe.com.ecocleany.ecosmart.operations.domain.model.CollectionRouteCheckpoint.CheckpointStatus;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface CollectionRouteCheckpointRepository extends JpaRepository<CollectionRouteCheckpoint, UUID> {
    List<CollectionRouteCheckpoint> findByRouteId(UUID routeId);
    Optional<CollectionRouteCheckpoint> findByRouteIdAndSmartBinId(UUID routeId, UUID smartBinId);
    long countByRouteIdAndStatus(UUID routeId, CheckpointStatus status);
}
