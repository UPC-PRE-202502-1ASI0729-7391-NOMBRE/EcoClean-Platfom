package pe.com.ecocleany.ecosmart.monitoring.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;

import java.util.Map;
import java.util.UUID;

@Value
@Builder
public class SmartBinStatusSummaryResponse {
    UUID municipalityId;
    Map<String, Long> byStatus;
    Double averageFillLevel;
    Integer totalBins;
}
