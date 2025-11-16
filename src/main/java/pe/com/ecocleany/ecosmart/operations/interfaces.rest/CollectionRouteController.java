package pe.com.ecocleany.ecosmart.operations.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ecocleany.ecosmart.iam.application.UserPrincipal;
import pe.com.ecocleany.ecosmart.operations.application.CollectionRouteService;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.dto.AssignOperatorRequest;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.dto.CollectionRouteResponse;
import pe.com.ecocleany.ecosmart.operations.interfaces.rest.dto.CreateRouteRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/operations/routes")
@RequiredArgsConstructor
@Tag(name = "BC: OPERATIONS")
@SecurityRequirement(name = "bearerAuth")
public class CollectionRouteController {

    private final CollectionRouteService routeService;

    @PostMapping
    public ResponseEntity<CollectionRouteResponse> create(@Valid @RequestBody CreateRouteRequest request) {
        return ResponseEntity.ok(routeService.create(request));
    }

    @PostMapping("/{routeId}/assign")
    public ResponseEntity<CollectionRouteResponse> assignOperator(@PathVariable UUID routeId,
                                                                  @Valid @RequestBody AssignOperatorRequest request) {
        return ResponseEntity.ok(routeService.assignOperator(routeId, request));
    }

    @PostMapping("/{routeId}/complete")
    public ResponseEntity<CollectionRouteResponse> complete(@PathVariable UUID routeId) {
        return ResponseEntity.ok(routeService.markCompleted(routeId));
    }

    @PostMapping("/{routeId}/bins/{smartBinId}/confirm")
    public ResponseEntity<CollectionRouteResponse> confirmCheckpoint(@PathVariable UUID routeId,
                                                                     @PathVariable UUID smartBinId,
                                                                     @AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(routeService.confirmCheckpoint(routeId, smartBinId, principal.id()));
    }

    @GetMapping
    public ResponseEntity<List<CollectionRouteResponse>> listByMunicipality(
            @RequestParam UUID municipalityId) {
        return ResponseEntity.ok(routeService.listByMunicipality(municipalityId));
    }

    @GetMapping("/me")
    public ResponseEntity<List<CollectionRouteResponse>> listForOperator(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(routeService.listForOperator(principal.id()));
    }
}
