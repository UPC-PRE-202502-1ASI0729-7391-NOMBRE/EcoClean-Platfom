package pe.com.ecocleany.ecosmart.admin.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ecocleany.ecosmart.admin.application.MunicipalityService;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.MunicipalityResponse;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/municipalities")
@RequiredArgsConstructor
@Tag(name = "BC: ADMIN")
@SecurityRequirement(name = "bearerAuth")
public class MunicipalityQueryController {

    private final MunicipalityService municipalityService;

    @GetMapping
    public ResponseEntity<List<MunicipalityResponse>> listActive(
            @RequestParam(value = "department", required = false) String department,
            @RequestParam(value = "province", required = false) String province) {
        return ResponseEntity.ok(municipalityService.listActive(department, province));
    }

    @GetMapping("/{id}")
    public ResponseEntity<MunicipalityResponse> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(municipalityService.findById(id));
    }
}
