package pe.com.ecocleany.ecosmart.admin.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ecocleany.ecosmart.admin.application.MunicipalityService;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.CreateMunicipalityRequest;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.MunicipalityResponse;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.UpdateMunicipalityRequest;
import pe.com.ecocleany.ecosmart.shared.interfaces.rest.dto.PageResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/municipalities")
@RequiredArgsConstructor
@Tag(name = "BC: ADMIN")
@SecurityRequirement(name = "bearerAuth")
public class MunicipalityController {

    private final MunicipalityService municipalityService;

    @GetMapping
    public ResponseEntity<PageResponse<MunicipalityResponse>> list(
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "province", required = false) String province,
            @RequestParam(value = "status", required = false) String status,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "10") int size) {
        Boolean active = null;
        if (status != null && !status.isBlank()) {
            active = switch (status.toLowerCase()) {
                case "active" -> true;
                case "inactive" -> false;
                default -> throw new org.springframework.web.server.ResponseStatusException(HttpStatus.BAD_REQUEST, "Estado inválido");
            };
        }
        return ResponseEntity.ok(municipalityService.findAll(department, province, active, page, size));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MunicipalityResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(municipalityService.findById(id));
    }

    @PostMapping
    public ResponseEntity<MunicipalityResponse> create(@Valid @RequestBody CreateMunicipalityRequest request) {
        return ResponseEntity.ok(municipalityService.create(request));
    }

    @PutMapping("/{id}")
    public ResponseEntity<MunicipalityResponse> update(@PathVariable UUID id,
                                                       @Valid @RequestBody UpdateMunicipalityRequest request) {
        return ResponseEntity.ok(municipalityService.update(id, request));
    }
}
