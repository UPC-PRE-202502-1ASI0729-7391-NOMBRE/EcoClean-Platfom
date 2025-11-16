package pe.com.ecocleany.ecosmart.smartbins.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "smart_bins")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class SmartBin {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 60)
    private String binCode;

    @Column(nullable = false, length = 60)
    private String district;

    @Column(nullable = false)
    private UUID municipalityId;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal latitude;

    @Column(nullable = false, precision = 9, scale = 6)
    private BigDecimal longitude;

    @Column(nullable = false, length = 180)
    private String address;

    @Column(nullable = false)
    private Integer capacityLiters;

    @Column(nullable = false)
    private Integer fillLevelPercentage;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private SmartBinStatus status;

    @Column(nullable = false)
    private Instant lastUpdateAt;

    public void updateStatus(int newFillLevel, SmartBinStatus newStatus) {
        this.fillLevelPercentage = Math.min(Math.max(newFillLevel, 0), 100);
        this.status = newStatus;
        this.lastUpdateAt = Instant.now();
    }

    public void markEmptied() {
        this.fillLevelPercentage = 0;
        this.status = SmartBinStatus.NORMAL;
        this.lastUpdateAt = Instant.now();
    }

    @PrePersist
    void onCreate() {
        if (lastUpdateAt == null) {
            lastUpdateAt = Instant.now();
        }
        if (status == null) {
            status = SmartBinStatus.NORMAL;
        }
        if (fillLevelPercentage == null) {
            fillLevelPercentage = 0;
        }
    }
}
