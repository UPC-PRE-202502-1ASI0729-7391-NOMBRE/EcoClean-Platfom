package pe.com.ecocleany.ecosmart.admin.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.admin.domain.model.RoleApplication;
import pe.com.ecocleany.ecosmart.admin.domain.model.RoleApplicationStatus;
import pe.com.ecocleany.ecosmart.admin.infrastructure.repositories.RoleApplicationRepository;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.CreateRoleApplicationRequest;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.RoleApplicationResponse;
import pe.com.ecocleany.ecosmart.iam.application.RoleManagementService;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleApplicationService {

    private final RoleApplicationRepository roleApplicationRepository;
    private final RoleManagementService roleManagementService;
    private final MunicipalityService municipalityService;

    public RoleApplicationResponse create(UUID userId, CreateRoleApplicationRequest request) {
        validateRole(request.getRole());
        validateFieldsByRole(request);

        if (roleApplicationRepository.existsByUserIdAndRequestedRoleAndStatus(
                userId, request.getRole(), RoleApplicationStatus.PENDING)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe una postulación pendiente para ese rol");
        }

        municipalityService.ensureExists(request.getMunicipalityId());

        RoleApplication application = RoleApplication.builder()
                .userId(userId)
                .municipalityId(request.getMunicipalityId())
                .requestedRole(request.getRole())
                .phoneNumber(request.getPhoneNumber())
                .drivingLicense(request.getDrivingLicense())
                .company(request.getCompany())
                .workDistrict(request.getWorkDistrict())
                .workCity(request.getWorkCity())
                .positionTitle(request.getPositionTitle())
                .municipalEntity(request.getMunicipalEntity())
                .entityRuc(request.getEntityRuc())
                .municipalWorkDistrict(request.getMunicipalWorkDistrict())
                .build();

        return mapToResponse(roleApplicationRepository.save(application));
    }

    @Transactional(readOnly = true)
    public List<RoleApplicationResponse> listByStatus(RoleApplicationStatus status, UUID municipalityId) {
        List<RoleApplication> applications = municipalityId == null
                ? roleApplicationRepository.findByStatus(status)
                : roleApplicationRepository.findByStatusAndMunicipalityId(status, municipalityId);

        return applications
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    public RoleApplicationResponse approve(UUID applicationId) {
        RoleApplication application = roleApplicationRepository.findByIdAndStatus(applicationId, RoleApplicationStatus.PENDING)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Postulación no encontrada o ya procesada"));

        application.markApproved();
        roleManagementService.assignRole(application.getUserId(), application.getRequestedRole());
        roleManagementService.assignMunicipality(application.getUserId(), application.getMunicipalityId());
        roleManagementService.updatePhoneNumber(application.getUserId(), application.getPhoneNumber());

        return mapToResponse(roleApplicationRepository.save(application));
    }

    public RoleApplicationResponse reject(UUID applicationId, String reason) {
        RoleApplication application = roleApplicationRepository.findByIdAndStatus(applicationId, RoleApplicationStatus.PENDING)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Postulación no encontrada o ya procesada"));

        application.markRejected(reason);
        return mapToResponse(roleApplicationRepository.save(application));
    }

    @Transactional(readOnly = true)
    public UUID getMunicipalityId(UUID applicationId) {
        return roleApplicationRepository.findById(applicationId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Postulación no encontrada"))
                .getMunicipalityId();
    }

    private void validateRole(RoleName roleName) {
        if (roleName != RoleName.OPERATOR && roleName != RoleName.MUNICIPAL_OFFICER) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Solo se aceptan postulaciones para OPERATOR o MUNICIPAL_OFFICER");
        }
    }

    private void validateFieldsByRole(CreateRoleApplicationRequest request) {
        if (request.getRole() == RoleName.OPERATOR) {
            require(request.getDrivingLicense(), "La licencia de conducir es obligatoria");
            require(request.getCompany(), "La empresa es obligatoria");
            require(request.getWorkDistrict(), "El distrito de trabajo es obligatorio");
            require(request.getWorkCity(), "La ciudad de trabajo es obligatoria");
        } else if (request.getRole() == RoleName.MUNICIPAL_OFFICER) {
            require(request.getPositionTitle(), "El cargo es obligatorio");
            require(request.getMunicipalEntity(), "La entidad municipal es obligatoria");
            require(request.getEntityRuc(), "El RUC de la entidad es obligatorio");
            require(request.getMunicipalWorkDistrict(), "El distrito de trabajo es obligatorio");
        }
    }

    private void require(String value, String message) {
        if (value == null || value.isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, message);
        }
    }

    private RoleApplicationResponse mapToResponse(RoleApplication application) {
        return RoleApplicationResponse.builder()
                .id(application.getId())
                .userId(application.getUserId())
                .role(application.getRequestedRole())
                .municipalityId(application.getMunicipalityId())
                .status(application.getStatus())
                .phoneNumber(application.getPhoneNumber())
                .drivingLicense(application.getDrivingLicense())
                .company(application.getCompany())
                .workDistrict(application.getWorkDistrict())
                .workCity(application.getWorkCity())
                .positionTitle(application.getPositionTitle())
                .municipalEntity(application.getMunicipalEntity())
                .entityRuc(application.getEntityRuc())
                .municipalWorkDistrict(application.getMunicipalWorkDistrict())
                .rejectionReason(application.getRejectionReason())
                .createdAt(application.getCreatedAt())
                .decidedAt(application.getDecidedAt())
                .build();
    }
}
