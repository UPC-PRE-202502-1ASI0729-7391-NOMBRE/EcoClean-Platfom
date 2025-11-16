package pe.com.ecocleany.ecosmart.profile.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.iam.application.UserMapper;
import pe.com.ecocleany.ecosmart.iam.domain.model.User;
import pe.com.ecocleany.ecosmart.iam.infrastructure.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.profile.domain.model.Profile;
import pe.com.ecocleany.ecosmart.profile.infrastructure.repositories.ProfileRepository;
import pe.com.ecocleany.ecosmart.profile.interfaces.rest.dto.ProfileResponse;
import pe.com.ecocleany.ecosmart.profile.interfaces.rest.dto.UpdateProfileRequest;

import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class ProfileApplicationService {

    private final ProfileRepository profileRepository;
    private final UserRepository userRepository;
    private final UserMapper userMapper;

    @Transactional(readOnly = true)
    public ProfileResponse getProfile(UUID userId) {
        User user = findUser(userId);
        Profile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> Profile.create(userId));

        if (profile.getId() == null) {
            profile = profileRepository.save(profile);
        }

        return buildResponse(user, profile);
    }

    public ProfileResponse updateProfile(UUID userId, UpdateProfileRequest request) {
        User user = findUser(userId);
        Profile profile = profileRepository.findByUserId(userId)
                .orElseGet(() -> Profile.create(userId));

        profile.updateDetails(
                request.getAvatarUrl(),
                request.getBio(),
                request.getOccupation(),
                request.getAddressReference(),
                request.getEmergencyContact(),
                request.getEmergencyPhone()
        );

        Profile saved = profileRepository.save(profile);
        return buildResponse(user, saved);
    }

    @Transactional(readOnly = true)
    public ProfileResponse getProfileByUserId(UUID userId) {
        return getProfile(userId);
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private ProfileResponse buildResponse(User user, Profile profile) {
        return ProfileResponse.builder()
                .user(userMapper.toResponse(user))
                .avatarUrl(profile.getAvatarUrl())
                .bio(profile.getBio())
                .occupation(profile.getOccupation())
                .addressReference(profile.getAddressReference())
                .emergencyContact(profile.getEmergencyContact())
                .emergencyPhone(profile.getEmergencyPhone())
                .updatedAt(profile.getUpdatedAt())
                .build();
    }
}
