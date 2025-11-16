package pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class CreateMunicipalityRequest {

    @NotBlank(message = "El nombre es obligatorio")
    @Size(max = 120)
    private String name;

    @NotBlank(message = "El departamento es obligatorio")
    private String department;

    @NotBlank(message = "La provincia es obligatoria")
    private String province;

    @NotBlank(message = "El distrito es obligatorio")
    private String district;

    @Size(max = 255, message = "La descripción no debe exceder 255 caracteres")
    private String description;
}
