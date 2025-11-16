package pe.com.ecocleany.ecosmart.admin.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.admin.application.RoleAdministrationService;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.ModifyRoleRequest;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.UserResponse;

import java.util.Locale;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "BC: ADMIN")
@SecurityRequirement(name = "bearerAuth")
public class RoleAdministrationController {

    private final RoleAdministrationService roleAdministrationService;

    @PostMapping("/{userId}/roles")
    public ResponseEntity<UserResponse> assignRole(@PathVariable UUID userId,
                                                   @Valid @RequestBody ModifyRoleRequest request) {
        return ResponseEntity.ok(roleAdministrationService.assignRole(userId, request.role()));
    }

    @DeleteMapping("/{userId}/roles/{role}")
    public ResponseEntity<UserResponse> removeRole(@PathVariable UUID userId,
                                                   @PathVariable("role") String role) {
        RoleName roleName = parseRole(role);
        return ResponseEntity.ok(roleAdministrationService.removeRole(userId, roleName));
    }

    private RoleName parseRole(String rawRole) {
        try {
            return RoleName.valueOf(rawRole.toUpperCase(Locale.ROOT));
        } catch (IllegalArgumentException ex) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol no válido, use uno de los valores permitidos");
        }
    }
}
