package pe.com.ecocleany.ecosmart.profile.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.profile.domain.model.Profile;

import java.util.Optional;
import java.util.UUID;

public interface ProfileRepository extends JpaRepository<Profile, UUID> {
    Optional<Profile> findByUserId(UUID userId);
}
