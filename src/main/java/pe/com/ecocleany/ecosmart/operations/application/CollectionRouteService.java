package pe.com.ecocleany.ecosmart.operations.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.admin.application.MunicipalityService;
import pe.com.ecocleany.ecosmart.operations.domain.model.CollectionRoute;
import pe.com.ecocleany.ecosmart.operations.domain.model.CollectionRouteCheckpoint;
import pe.com.ecocleany.ecosmart.operations.domain.model.CollectionRouteCheckpoint.CheckpointStatus;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.dto.AssignOperatorRequest;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.dto.CollectionRouteResponse;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.dto.CreateRouteRequest;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.dto.RouteCheckpointResponse;
import pe.com.ecocleany.ecosmart.operations.infrastructure.repositories.CollectionRouteCheckpointRepository;
import pe.com.ecocleany.ecosmart.operations.infrastructure.repositories.CollectionRouteRepository;
import pe.com.ecocleany.ecosmart.smartbins.application.SmartBinService;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class CollectionRouteService {

    private final CollectionRouteRepository routeRepository;
    private final CollectionRouteCheckpointRepository checkpointRepository;
    private final MunicipalityService municipalityService;
    private final SmartBinService smartBinService;

    public CollectionRouteResponse create(CreateRouteRequest request) {
        municipalityService.ensureExists(request.getMunicipalityId());
        CollectionRoute route = CollectionRoute.builder()
                .name(request.getName())
                .municipalityId(request.getMunicipalityId())
                .smartBinIds(request.getSmartBinIds())
                .scheduledAt(request.getScheduledAt() != null ? request.getScheduledAt() : Instant.now())
                .status(CollectionRoute.RouteStatus.PLANNED)
                .build();

        CollectionRoute saved = routeRepository.save(route);
        request.getSmartBinIds().forEach(binId -> checkpointRepository.save(
                CollectionRouteCheckpoint.builder()
                        .route(saved)
                        .smartBinId(binId)
                        .status(CheckpointStatus.PENDING)
                        .build()
        ));

        return mapToResponse(saved);
    }

    public CollectionRouteResponse assignOperator(UUID routeId, AssignOperatorRequest request) {
        CollectionRoute route = load(routeId);
        route.assignOperator(request.operatorId());
        routeRepository.save(route);
        return mapToResponse(route);
    }

    public CollectionRouteResponse markCompleted(UUID routeId) {
        CollectionRoute route = load(routeId);
        route.markCompleted();
        routeRepository.save(route);
        route.getSmartBinIds().forEach(smartBinService::markEmpty);
        return mapToResponse(route);
    }

    public CollectionRouteResponse confirmCheckpoint(UUID routeId, UUID smartBinId, UUID operatorId) {
        CollectionRoute route = load(routeId);
        if (route.getOperatorId() == null || !route.getOperatorId().equals(operatorId)) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "La ruta no está asignada a este operador");
        }
        CollectionRouteCheckpoint checkpoint = checkpointRepository.findByRouteIdAndSmartBinId(routeId, smartBinId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Checkpoint no encontrado"));
        if (checkpoint.getStatus() == CheckpointStatus.COMPLETED) {
            return mapToResponse(route);
        }

        checkpoint.markCompleted(operatorId);
        checkpointRepository.save(checkpoint);
        smartBinService.markEmpty(smartBinId);

        if (route.getStatus() == CollectionRoute.RouteStatus.ASSIGNED) {
            route.markInProgress();
            routeRepository.save(route);
        }

        long pending = checkpointRepository.countByRouteIdAndStatus(routeId, CheckpointStatus.PENDING);
        if (pending == 0) {
            route.markCompleted();
            routeRepository.save(route);
        }

        return mapToResponse(route);
    }

    @Transactional(readOnly = true)
    public List<CollectionRouteResponse> listByMunicipality(UUID municipalityId) {
        return routeRepository.findByMunicipalityId(municipalityId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<CollectionRouteResponse> listForOperator(UUID operatorId) {
        return routeRepository.findByOperatorId(operatorId)
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    private CollectionRoute load(UUID id) {
        return routeRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Ruta no encontrada"));
    }

    private CollectionRouteResponse mapToResponse(CollectionRoute route) {
        return CollectionRouteResponse.builder()
                .id(route.getId())
                .name(route.getName())
                .municipalityId(route.getMunicipalityId())
                .smartBinIds(route.getSmartBinIds())
                .scheduledAt(route.getScheduledAt())
                .status(route.getStatus())
                .operatorId(route.getOperatorId())
                .checkpoints(checkpointRepository.findByRouteId(route.getId())
                        .stream()
                        .map(cp -> RouteCheckpointResponse.builder()
                                .smartBinId(cp.getSmartBinId())
                                .status(cp.getStatus())
                                .visitedAt(cp.getVisitedAt())
                                .confirmedBy(cp.getConfirmedBy())
                                .build())
                        .toList())
                .build();
    }
}
