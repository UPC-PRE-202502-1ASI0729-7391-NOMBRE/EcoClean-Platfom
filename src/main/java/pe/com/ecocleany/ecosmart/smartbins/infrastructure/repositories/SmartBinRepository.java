package pe.com.ecocleany.ecosmart.smartbins.infrastructure.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import pe.com.ecocleany.ecosmart.smartbins.domain.model.SmartBin;

import java.util.UUID;

public interface SmartBinRepository extends JpaRepository<SmartBin, UUID> {
}
