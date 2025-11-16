package pe.com.ecocleany.ecosmart.admin.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ecocleany.ecosmart.admin.application.RoleAssignmentService;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.RoleAssignmentResponse;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.UpdateUserRoleRequest;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
@Tag(name = "BC: ADMIN")
@SecurityRequirement(name = "bearerAuth")
public class AdminUserDirectoryController {

    private final RoleAssignmentService roleAssignmentService;

    @GetMapping
    public ResponseEntity<List<RoleAssignmentResponse>> list() {
        return ResponseEntity.ok(roleAssignmentService.listAssignments());
    }

    @PutMapping("/{userId}/role")
    public ResponseEntity<RoleAssignmentResponse> updateRole(@PathVariable UUID userId,
                                                             @Valid @RequestBody UpdateUserRoleRequest request) {
        return ResponseEntity.ok(roleAssignmentService.updateRole(userId, request.role()));
    }
}
