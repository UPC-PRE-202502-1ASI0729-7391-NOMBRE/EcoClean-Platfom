package pe.com.ecocleany.ecosmart.admin.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import pe.com.ecocleany.ecosmart.admin.domain.model.Municipality;

import java.util.UUID;

public interface MunicipalityRepository extends JpaRepository<Municipality, UUID>, JpaSpecificationExecutor<Municipality> {
    boolean existsByNameIgnoreCase(String name);
}
