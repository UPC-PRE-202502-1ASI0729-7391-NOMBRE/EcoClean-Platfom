package pe.com.ecocleany.ecosmart.iam.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.iam.domain.model.User;

import java.util.Optional;
import java.util.UUID;

public interface UserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
    boolean existsByDni(String dni);
}
