package pe.com.ecocleany.ecosmart.profile.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.com.ecocleany.ecosmart.profile.domain.model.Profile;
import pe.com.ecocleany.ecosmart.profile.infrastructure.repositories.ProfileRepository;
import pe.com.ecocleany.ecosmart.shared.application.ProfileInitializer;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class ProfileInitializerImpl implements ProfileInitializer {

    private final ProfileRepository profileRepository;

    @Override
    @Transactional
    public void ensureProfile(UUID userId) {
        profileRepository.findByUserId(userId)
                .orElseGet(() -> profileRepository.save(Profile.create(userId)));
    }
}
