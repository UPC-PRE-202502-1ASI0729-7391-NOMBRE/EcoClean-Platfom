package pe.com.ecocleany.ecosmart.smartbins.application;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.smartbins.domain.model.SmartBin;
import pe.com.ecocleany.ecosmart.smartbins.domain.model.SmartBinStatus;
import pe.com.ecocleany.ecosmart.smartbins.infrastructure.repositories.SmartBinRepository;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

@Service
@RequiredArgsConstructor
@Slf4j
public class SmartBinSimulationService {

    private final SmartBinRepository smartBinRepository;

    @Scheduled(fixedDelayString = "${smartbins.simulation.interval-ms:30000}")
    public void simulateFillLevels() {
        List<SmartBin> bins = smartBinRepository.findAll();
        ThreadLocalRandom random = ThreadLocalRandom.current();

        bins.stream()
                .filter(bin -> bin.getStatus() != SmartBinStatus.OUT_OF_SERVICE)
                .forEach(bin -> {
                    int increment = random.nextInt(0, 16); // 0-15%
                    int newFill = Math.min(bin.getFillLevelPercentage() + increment, 100);
                    SmartBinStatus status = calculateStatus(newFill);
                    bin.updateStatus(newFill, status);
                });

        if (!bins.isEmpty()) {
            smartBinRepository.saveAll(bins);
            log.debug("Simulación de SmartBins ejecutada para {} tachos", bins.size());
        }
    }

    private SmartBinStatus calculateStatus(int fillLevel) {
        if (fillLevel >= 90) {
            return SmartBinStatus.FULL;
        }
        if (fillLevel >= 60) {
            return SmartBinStatus.ALMOST_FULL;
        }
        return SmartBinStatus.NORMAL;
    }
}
