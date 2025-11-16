package pe.com.ecocleany.ecosmart.admin.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ecocleany.ecosmart.admin.application.MunicipalityService;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.UpdateUserMunicipalityRequest;
import pe.com.ecocleany.ecosmart.iam.application.RoleManagementService;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.UserResponse;

import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "BC: ADMIN")
@SecurityRequirement(name = "bearerAuth")
public class UserManagementController {

    private final RoleManagementService roleManagementService;
    private final MunicipalityService municipalityService;

    @PutMapping("/{userId}/municipality")
    public ResponseEntity<UserResponse> updateMunicipality(@PathVariable UUID userId,
                                                           @Valid @RequestBody UpdateUserMunicipalityRequest request) {
        municipalityService.ensureExists(request.municipalityId());
        return ResponseEntity.ok(roleManagementService.forceAssignMunicipality(userId, request.municipalityId()));
    }
}
