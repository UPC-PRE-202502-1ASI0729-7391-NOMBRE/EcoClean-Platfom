package pe.com.ecocleany.ecosmart.admin.interfaces.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.admin.application.RoleApplicationService;
import pe.com.ecocleany.ecosmart.admin.domain.model.RoleApplicationStatus;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.CreateRoleApplicationRequest;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.RejectRoleApplicationRequest;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.RoleApplicationResponse;
import pe.com.ecocleany.ecosmart.iam.application.UserPrincipal;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/role-applications")
@RequiredArgsConstructor
@Tag(name = "BC: ADMIN")
@SecurityRequirement(name = "bearerAuth")
public class RoleApplicationController {

    private final RoleApplicationService roleApplicationService;

    @PostMapping
    public ResponseEntity<RoleApplicationResponse> create(@AuthenticationPrincipal UserPrincipal principal,
                                                          @Valid @RequestBody CreateRoleApplicationRequest request) {
        return ResponseEntity.ok(roleApplicationService.create(principal.id(), request));
    }

    @GetMapping
    public ResponseEntity<List<RoleApplicationResponse>> listByStatus(
            @Parameter(description = "Estado de la postulación", required = true)
            @RequestParam RoleApplicationStatus status,
            @Parameter(description = "Filtrar por municipio (solo aplica para funcionarios municipales)")
            @RequestParam(required = false) UUID municipalityId,
            @AuthenticationPrincipal UserPrincipal principal) {
        UUID resolvedMunicipalityId = resolveMunicipalityFilter(principal, municipalityId);
        return ResponseEntity.ok(roleApplicationService.listByStatus(status, resolvedMunicipalityId));
    }

    @PostMapping("/{applicationId}/approve")
    public ResponseEntity<RoleApplicationResponse> approve(@PathVariable UUID applicationId,
                                                           @AuthenticationPrincipal UserPrincipal principal) {
        verifyAuthorizationForApplication(principal, applicationId);
        return ResponseEntity.ok(roleApplicationService.approve(applicationId));
    }

    @PostMapping("/{applicationId}/reject")
    public ResponseEntity<RoleApplicationResponse> reject(@PathVariable UUID applicationId,
                                                          @Valid @RequestBody RejectRoleApplicationRequest request,
                                                          @AuthenticationPrincipal UserPrincipal principal) {
        verifyAuthorizationForApplication(principal, applicationId);
        return ResponseEntity.ok(roleApplicationService.reject(applicationId, request.reason()));
    }

    private UUID resolveMunicipalityFilter(UserPrincipal principal, UUID requestedMunicipalityId) {
        boolean isAdmin = hasAuthority(principal, "ROLE_ADMIN");
        if (isAdmin) {
            return requestedMunicipalityId;
        }
        if (hasAuthority(principal, "ROLE_MUNICIPAL_OFFICER")) {
            if (principal.municipalityId() == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe tener un municipio asignado");
            }
            return principal.municipalityId();
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene acceso a esta operación");
    }

    private void verifyAuthorizationForApplication(UserPrincipal principal, UUID applicationId) {
        if (hasAuthority(principal, "ROLE_ADMIN")) {
            return;
        }
        if (hasAuthority(principal, "ROLE_MUNICIPAL_OFFICER")) {
            UUID municipalityId = principal.municipalityId();
            if (municipalityId == null) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Debe tener un municipio asignado");
            }
            UUID applicationMunicipality = roleApplicationService.getMunicipalityId(applicationId);
            if (!municipalityId.equals(applicationMunicipality)) {
                throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No puede gestionar postulaciones de otro municipio");
            }
            return;
        }
        throw new ResponseStatusException(HttpStatus.FORBIDDEN, "No tiene acceso a esta operación");
    }

    private boolean hasAuthority(UserPrincipal principal, String authority) {
        return principal.authorities().stream().anyMatch(a -> a.getAuthority().equals(authority));
    }
}
