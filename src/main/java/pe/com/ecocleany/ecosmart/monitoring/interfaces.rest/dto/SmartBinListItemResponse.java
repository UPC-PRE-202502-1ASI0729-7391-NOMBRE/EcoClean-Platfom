package pe.com.ecocleany.ecosmart.monitoring.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;
import pe.com.ecocleany.ecosmart.smartbins.domain.model.SmartBinStatus;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class SmartBinListItemResponse {
    UUID id;
    String binCode;
    UUID municipalityId;
    String district;
    BigDecimal latitude;
    BigDecimal longitude;
    String address;
    Integer fillLevelPercentage;
    SmartBinStatus status;
    Instant lastUpdateAt;
}
