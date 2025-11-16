package pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

import java.util.UUID;

@Data
public class CreateRoleApplicationRequest {

    @NotNull(message = "El rol solicitado es obligatorio")
    private RoleName role;

    @NotNull(message = "El municipio es obligatorio")
    private UUID municipalityId;

    @NotBlank(message = "El celular es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El celular debe tener 9 dígitos")
    private String phoneNumber;

    // Operator
    @Size(max = 120, message = "La licencia no debe exceder 120 caracteres")
    private String drivingLicense;

    @Size(max = 120, message = "La empresa no debe exceder 120 caracteres")
    private String company;

    @Size(max = 60, message = "El distrito de trabajo no debe exceder 60 caracteres")
    private String workDistrict;

    @Size(max = 60, message = "La ciudad de trabajo no debe exceder 60 caracteres")
    private String workCity;

    // Officer
    @Size(max = 120, message = "El cargo no debe exceder 120 caracteres")
    private String positionTitle;

    @Size(max = 160, message = "La entidad municipal no debe exceder 160 caracteres")
    private String municipalEntity;

    @Pattern(regexp = "(^$|\\d{11})", message = "El RUC debe contener 11 dígitos")
    private String entityRuc;

    @Size(max = 60, message = "El distrito municipal no debe exceder 60 caracteres")
    private String municipalWorkDistrict;
}
