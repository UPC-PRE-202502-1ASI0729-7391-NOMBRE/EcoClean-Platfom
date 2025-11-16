package pe.com.ecocleany.ecosmart.admin.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "role_applications")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RoleApplication {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false)
    private UUID userId;

    @Column(nullable = false)
    private UUID municipalityId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 40)
    private RoleName requestedRole;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RoleApplicationStatus status;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    // Operator fields
    @Column(length = 120)
    private String drivingLicense;

    @Column(length = 120)
    private String company;

    @Column(length = 60)
    private String workDistrict;

    @Column(length = 60)
    private String workCity;

    // Municipal officer fields
    @Column(length = 120)
    private String positionTitle;

    @Column(length = 160)
    private String municipalEntity;

    @Column(length = 11)
    private String entityRuc;

    @Column(length = 60)
    private String municipalWorkDistrict;

    @Column(length = 120)
    private String rejectionReason;

    @Column(nullable = false, updatable = false)
    private Instant createdAt;

    @Column
    private Instant decidedAt;

    public void markApproved() {
        this.status = RoleApplicationStatus.APPROVED;
        this.decidedAt = Instant.now();
        this.rejectionReason = null;
    }

    public void markRejected(String reason) {
        this.status = RoleApplicationStatus.REJECTED;
        this.decidedAt = Instant.now();
        this.rejectionReason = reason;
    }

    @PrePersist
    void onCreate() {
        this.createdAt = Instant.now();
        this.status = RoleApplicationStatus.PENDING;
    }
}
