package pe.com.ecocleany.ecosmart.smartbins.interfaces.rest;

import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.DecimalMax;
import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.NotNull;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ecocleany.ecosmart.smartbins.application.SmartBinService;
import pe.com.ecocleany.ecosmart.smartbins.interfaces.rest.dto.CreateSmartBinRequest;
import pe.com.ecocleany.ecosmart.smartbins.interfaces.rest.dto.SmartBinResponse;

import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/smartbins")
@RequiredArgsConstructor
@Tag(name = "BC: SMARTBINS")
@SecurityRequirement(name = "bearerAuth")
public class SmartBinController {

    private final SmartBinService smartBinService;

    @GetMapping
    public ResponseEntity<List<SmartBinResponse>> listAll(
            @RequestParam(name = "municipalityId", required = false) UUID municipalityId) {
        return ResponseEntity.ok(smartBinService.findAll(municipalityId));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SmartBinResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(smartBinService.findById(id));
    }

    @GetMapping("/nearby")
    public ResponseEntity<List<SmartBinResponse>> findNearby(
            @Parameter(description = "Latitud en grados decimales")
            @RequestParam @NotNull @DecimalMin("-90.0") @DecimalMax("90.0") BigDecimal lat,
            @Parameter(description = "Longitud en grados decimales")
            @RequestParam @NotNull @DecimalMin("-180.0") @DecimalMax("180.0") BigDecimal lng,
            @Parameter(description = "Radio en metros", example = "500")
            @RequestParam(defaultValue = "500") double radiusMeters) {
        return ResponseEntity.ok(smartBinService.findNearby(lat, lng, radiusMeters));
    }

    @PostMapping
    public ResponseEntity<SmartBinResponse> create(@Valid @RequestBody CreateSmartBinRequest request) {
        return ResponseEntity.ok(smartBinService.create(request));
    }

    @PostMapping("/{id}/empty")
    public ResponseEntity<SmartBinResponse> markEmpty(@PathVariable UUID id) {
        return ResponseEntity.ok(smartBinService.markEmpty(id));
    }
}
