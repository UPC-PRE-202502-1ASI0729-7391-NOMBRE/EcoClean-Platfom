package pe.com.ecocleany.ecosmart.iam.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.iam.domain.model.Role;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(RoleName name);
}
