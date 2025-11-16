package pe.com.ecocleany.ecosmart.admin.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ecocleany.ecosmart.admin.application.BoundedContextCatalogService;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.BoundedContextResponse;

import java.util.List;

@RestController
@RequestMapping("/api/admin/applications")
@RequiredArgsConstructor
@Tag(name = "BC: ADMIN")
@SecurityRequirement(name = "bearerAuth")
public class AdminApplicationController {

    private final BoundedContextCatalogService catalogService;

    @GetMapping
    public ResponseEntity<List<BoundedContextResponse>> list() {
        return ResponseEntity.ok(catalogService.listAll());
    }
}
