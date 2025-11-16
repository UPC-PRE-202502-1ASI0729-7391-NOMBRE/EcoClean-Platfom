package pe.com.ecocleany.ecosmart.admin.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ecocleany.ecosmart.admin.application.RolePolicyCatalogService;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.RolePolicyResponse;

import java.util.List;

@RestController
@RequestMapping("/api/admin/roles/policies")
@RequiredArgsConstructor
@Tag(name = "BC: ADMIN")
@SecurityRequirement(name = "bearerAuth")
public class RolePolicyController {

    private final RolePolicyCatalogService catalogService;

    @GetMapping
    public ResponseEntity<List<RolePolicyResponse>> list() {
        return ResponseEntity.ok(catalogService.listAll());
    }
}
