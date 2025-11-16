package pe.com.ecocleany.ecosmart.monitoring.application;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.monitoring.interfaces.rest.dto.SmartBinListItemResponse;
import pe.com.ecocleany.ecosmart.monitoring.interfaces.rest.dto.SmartBinStatusSummaryResponse;
import pe.com.ecocleany.ecosmart.smartbins.domain.model.SmartBin;
import pe.com.ecocleany.ecosmart.smartbins.infrastructure.repositories.SmartBinRepository;

import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

import static org.springframework.http.HttpStatus.BAD_REQUEST;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MonitoringService {

    private final SmartBinRepository smartBinRepository;

    public SmartBinStatusSummaryResponse smartBinSummary(UUID municipalityId) {
        List<SmartBin> bins = loadByMunicipality(municipalityId);

        Map<String, Long> grouped = bins.stream()
                .collect(Collectors.groupingBy(bin -> bin.getStatus().name(), Collectors.counting()));

        double avgFill = bins.stream()
                .mapToInt(SmartBin::getFillLevelPercentage)
                .average()
                .orElse(0);

        return SmartBinStatusSummaryResponse.builder()
                .municipalityId(municipalityId)
                .byStatus(grouped)
                .averageFillLevel(Math.round(avgFill * 100.0) / 100.0)
                .totalBins(bins.size())
                .build();
    }

    public List<SmartBinListItemResponse> smartBins(UUID municipalityId, SmartBinStatusFilter filter) {
        List<SmartBin> bins = loadByMunicipality(municipalityId);
        return bins.stream()
                .filter(bin -> filter.matches(bin.getStatus(), bin.getLastUpdateAt()))
                .map(this::mapToListItem)
                .toList();
    }

    private List<SmartBin> loadByMunicipality(UUID municipalityId) {
        if (municipalityId == null) {
            throw new ResponseStatusException(BAD_REQUEST, "Municipio es obligatorio para el panel");
        }
        return smartBinRepository.findAll()
                .stream()
                .filter(bin -> municipalityId.equals(bin.getMunicipalityId()))
                .toList();
    }

    private SmartBinListItemResponse mapToListItem(SmartBin bin) {
        return SmartBinListItemResponse.builder()
                .id(bin.getId())
                .binCode(bin.getBinCode())
                .municipalityId(bin.getMunicipalityId())
                .district(bin.getDistrict())
                .latitude(bin.getLatitude())
                .longitude(bin.getLongitude())
                .address(bin.getAddress())
                .fillLevelPercentage(bin.getFillLevelPercentage())
                .status(bin.getStatus())
                .lastUpdateAt(bin.getLastUpdateAt())
                .build();
    }

    public enum SmartBinStatusFilter {
        ALL,
        FULL_ONLY,
        CRITICAL_LAST_HOUR;

        public boolean matches(pe.com.ecocleany.ecosmart.smartbins.domain.model.SmartBinStatus status, Instant lastUpdateAt) {
            return switch (this) {
                case ALL -> true;
                case FULL_ONLY -> status == pe.com.ecocleany.ecosmart.smartbins.domain.model.SmartBinStatus.FULL;
                case CRITICAL_LAST_HOUR -> status == pe.com.ecocleany.ecosmart.smartbins.domain.model.SmartBinStatus.FULL
                        || (status == pe.com.ecocleany.ecosmart.smartbins.domain.model.SmartBinStatus.ALMOST_FULL
                        && lastUpdateAt != null && lastUpdateAt.isAfter(Instant.now().minusSeconds(3600)));
            };
        }
    }
}
