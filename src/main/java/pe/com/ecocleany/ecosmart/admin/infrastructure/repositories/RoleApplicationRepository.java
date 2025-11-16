package pe.com.ecocleany.ecosmart.admin.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.admin.domain.model.RoleApplication;
import pe.com.ecocleany.ecosmart.admin.domain.model.RoleApplicationStatus;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface RoleApplicationRepository extends JpaRepository<RoleApplication, UUID> {
    boolean existsByUserIdAndRequestedRoleAndStatus(UUID userId, RoleName requestedRole, RoleApplicationStatus status);
    List<RoleApplication> findByStatus(RoleApplicationStatus status);
    Optional<RoleApplication> findByIdAndStatus(UUID id, RoleApplicationStatus status);
    List<RoleApplication> findByStatusAndMunicipalityId(RoleApplicationStatus status, UUID municipalityId);
}
