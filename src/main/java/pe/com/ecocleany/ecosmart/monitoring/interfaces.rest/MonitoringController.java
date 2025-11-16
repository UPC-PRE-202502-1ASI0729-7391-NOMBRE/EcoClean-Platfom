package pe.com.ecocleany.ecosmart.monitoring.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.iam.application.UserPrincipal;
import pe.com.ecocleany.ecosmart.monitoring.application.MonitoringService;
import pe.com.ecocleany.ecosmart.monitoring.interfaces.rest.dto.SmartBinListItemResponse;
import pe.com.ecocleany.ecosmart.monitoring.interfaces.rest.dto.SmartBinStatusSummaryResponse;

import java.util.List;
import java.util.UUID;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@RestController
@RequestMapping("/api/monitoring")
@RequiredArgsConstructor
@Tag(name = "BC: MONITORING")
@SecurityRequirement(name = "bearerAuth")
public class MonitoringController {

    private final MonitoringService monitoringService;

    @GetMapping("/smartbins/summary")
    public ResponseEntity<SmartBinStatusSummaryResponse> summary(
            @RequestParam(value = "municipalityId", required = false) UUID municipalityId,
            @AuthenticationPrincipal UserPrincipal principal) {
        UUID resolvedMunicipality = resolveMunicipality(principal, municipalityId);
        return ResponseEntity.ok(monitoringService.smartBinSummary(resolvedMunicipality));
    }

    @GetMapping("/smartbins")
    public ResponseEntity<List<SmartBinListItemResponse>> listSmartBins(
            @RequestParam(value = "municipalityId", required = false) UUID municipalityId,
            @RequestParam(value = "filter", defaultValue = "ALL") MonitoringService.SmartBinStatusFilter filter,
            @AuthenticationPrincipal UserPrincipal principal) {
        UUID resolvedMunicipality = resolveMunicipality(principal, municipalityId);
        return ResponseEntity.ok(monitoringService.smartBins(resolvedMunicipality, filter));
    }

    private UUID resolveMunicipality(UserPrincipal principal, UUID requestMunicipality) {
        if (requestMunicipality != null) {
            return requestMunicipality;
        }
        if (principal.municipalityId() != null) {
            return principal.municipalityId();
        }
        if (principal.authorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            throw new ResponseStatusException(BAD_REQUEST, "Debe indicar un municipio");
        }
        throw new ResponseStatusException(BAD_REQUEST, "Debe tener un municipio asignado o indicarlo explícitamente");
    }
}
