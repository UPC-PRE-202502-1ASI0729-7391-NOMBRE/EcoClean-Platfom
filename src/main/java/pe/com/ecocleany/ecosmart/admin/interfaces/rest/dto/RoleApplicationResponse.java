package pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;
import pe.com.ecocleany.ecosmart.admin.domain.model.RoleApplicationStatus;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

import java.time.Instant;
import java.util.UUID;

@Value
@Builder
public class RoleApplicationResponse {
    UUID id;
    UUID userId;
    RoleName role;
    UUID municipalityId;
    RoleApplicationStatus status;
    String phoneNumber;
    String drivingLicense;
    String company;
    String workDistrict;
    String workCity;
    String positionTitle;
    String municipalEntity;
    String entityRuc;
    String municipalWorkDistrict;
    String rejectionReason;
    Instant createdAt;
    Instant decidedAt;
}
