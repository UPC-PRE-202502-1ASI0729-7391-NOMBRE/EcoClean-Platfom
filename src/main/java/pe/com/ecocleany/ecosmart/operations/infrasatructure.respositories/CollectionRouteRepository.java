package pe.com.ecocleany.ecosmart.operations.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.operations.domain.model.CollectionRoute;

import java.util.List;
import java.util.UUID;

public interface CollectionRouteRepository extends JpaRepository<CollectionRoute, UUID> {
    List<CollectionRoute> findByMunicipalityId(UUID municipalityId);
    List<CollectionRoute> findByOperatorId(UUID operatorId);
}
