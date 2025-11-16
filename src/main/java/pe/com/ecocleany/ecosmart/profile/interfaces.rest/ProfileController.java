package pe.com.ecocleany.ecosmart.profile.interfaces.rest;

import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import pe.com.ecocleany.ecosmart.iam.application.UserPrincipal;
import pe.com.ecocleany.ecosmart.profile.application.ProfileApplicationService;
import pe.com.ecocleany.ecosmart.profile.interfaces.rest.dto.ProfileResponse;
import pe.com.ecocleany.ecosmart.profile.interfaces.rest.dto.UpdateProfileRequest;

@RestController
@RequestMapping("/api/profile")
@RequiredArgsConstructor
@Tag(name = "BC: PROFILE")
@SecurityRequirement(name = "bearerAuth")
public class ProfileController {

    private final ProfileApplicationService profileApplicationService;

    @GetMapping("/me")
    public ResponseEntity<ProfileResponse> me(@AuthenticationPrincipal UserPrincipal principal) {
        return ResponseEntity.ok(profileApplicationService.getProfile(principal.id()));
    }

    @PutMapping("/me")
    public ResponseEntity<ProfileResponse> update(@AuthenticationPrincipal UserPrincipal principal,
                                                  @Valid @RequestBody UpdateProfileRequest request) {
        return ResponseEntity.ok(profileApplicationService.updateProfile(principal.id(), request));
    }

    @GetMapping("/admin/{userId}")
    public ResponseEntity<ProfileResponse> findByUserId(@PathVariable("userId") java.util.UUID userId) {
        return ResponseEntity.ok(profileApplicationService.getProfileByUserId(userId));
    }
}
