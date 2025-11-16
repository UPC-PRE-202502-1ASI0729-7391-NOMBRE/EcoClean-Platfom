package pe.com.ecocleany.ecosmart.iam.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ecocleany.ecosmart.iam.application.AuthApplicationService;
import pe.com.ecocleany.ecosmart.iam.application.UserPrincipal;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.AuthResponse;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.LoginRequest;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.RegisterUserRequest;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.UserResponse;

@RestController
@RequestMapping("/api/iam/auth")
@RequiredArgsConstructor
@Tag(name = "BC: IAM")
public class IamController {

    private final AuthApplicationService authApplicationService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Valid @RequestBody RegisterUserRequest request) {
        return ResponseEntity.ok(authApplicationService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@Valid @RequestBody LoginRequest request) {
        return ResponseEntity.ok(authApplicationService.login(request));
    }

    @GetMapping("/me")
    @SecurityRequirement(name = "bearerAuth")
    public ResponseEntity<UserResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(authApplicationService.me(principal.id()));
    }
}
