package pe.com.ecocleany.ecosmart.iam.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.iam.domain.model.UserRole;

public interface UserRoleRepository extends JpaRepository<UserRole, Long> {
}
