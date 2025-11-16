package pe.com.ecocleany.ecosmart.smartbins.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.admin.application.MunicipalityService;
import pe.com.ecocleany.ecosmart.smartbins.domain.model.SmartBin;
import pe.com.ecocleany.ecosmart.smartbins.domain.model.SmartBinStatus;
import pe.com.ecocleany.ecosmart.smartbins.infrastructure.repositories.SmartBinRepository;
import pe.com.ecocleany.ecosmart.smartbins.interfaces.rest.dto.CreateSmartBinRequest;
import pe.com.ecocleany.ecosmart.smartbins.interfaces.rest.dto.SmartBinResponse;

import java.math.BigDecimal;
import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class SmartBinService {

    private final SmartBinRepository smartBinRepository;
    private final MunicipalityService municipalityService;

    @Transactional(readOnly = true)
    public List<SmartBinResponse> findAll(UUID municipalityId) {
        return smartBinRepository.findAll()
                .stream()
                .filter(bin -> municipalityId == null || municipalityId.equals(bin.getMunicipalityId()))
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public SmartBinResponse findById(UUID id) {
        return mapToResponse(loadSmartBin(id));
    }

    public SmartBinResponse create(CreateSmartBinRequest request) {
        SmartBin smartBin = SmartBin.builder()
                .binCode(request.getBinCode())
                .municipalityId(municipalityService.ensureExists(request.getMunicipalityId()).getId())
                .district(request.getDistrict())
                .latitude(request.getLatitude())
                .longitude(request.getLongitude())
                .address(request.getAddress())
                .capacityLiters(request.getCapacityLiters())
                .fillLevelPercentage(0)
                .status(SmartBinStatus.NORMAL)
                .build();
        return mapToResponse(smartBinRepository.save(smartBin));
    }

    public SmartBinResponse markEmpty(UUID id) {
        SmartBin smartBin = loadSmartBin(id);
        smartBin.markEmptied();
        return mapToResponse(smartBinRepository.save(smartBin));
    }

    @Transactional(readOnly = true)
    public List<SmartBinResponse> findNearby(BigDecimal latitude, BigDecimal longitude, double radiusMeters) {
        return smartBinRepository.findAll()
                .stream()
                .filter(bin -> distanceMeters(latitude.doubleValue(), longitude.doubleValue(),
                        bin.getLatitude().doubleValue(), bin.getLongitude().doubleValue()) <= radiusMeters)
                .sorted(Comparator.comparing(SmartBin::getDistrict))
                .map(this::mapToResponse)
                .toList();
    }

    private SmartBin loadSmartBin(UUID id) {
        return smartBinRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "SmartBin no encontrado"));
    }

    private SmartBinResponse mapToResponse(SmartBin smartBin) {
        return SmartBinResponse.builder()
                .id(smartBin.getId())
                .binCode(smartBin.getBinCode())
                .municipalityId(smartBin.getMunicipalityId())
                .district(smartBin.getDistrict())
                .latitude(smartBin.getLatitude())
                .longitude(smartBin.getLongitude())
                .address(smartBin.getAddress())
                .capacityLiters(smartBin.getCapacityLiters())
                .fillLevelPercentage(smartBin.getFillLevelPercentage())
                .status(smartBin.getStatus())
                .lastUpdateAt(smartBin.getLastUpdateAt())
                .build();
    }

    private double distanceMeters(double lat1, double lon1, double lat2, double lon2) {
        double earthRadius = 6371000; // meters
        double dLat = Math.toRadians(lat2 - lat1);
        double dLon = Math.toRadians(lon2 - lon1);
        double a = Math.sin(dLat / 2) * Math.sin(dLat / 2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLon / 2) * Math.sin(dLon / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return earthRadius * c;
    }
}
