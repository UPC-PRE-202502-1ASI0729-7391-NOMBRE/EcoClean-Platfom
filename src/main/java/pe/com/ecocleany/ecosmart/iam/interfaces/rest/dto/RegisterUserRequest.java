package pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class RegisterUserRequest {

    @Email(message = "El correo electrónico no tiene un formato válido")
    @NotBlank(message = "El correo electrónico es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Size(min = 8, max = 80, message = "La contraseña debe tener entre 8 y 80 caracteres")
    @Schema(example = "string")
    private String password;

    @NotBlank(message = "Los nombres son obligatorios")
    @Size(max = 80, message = "Los nombres no deben exceder 80 caracteres")
    private String firstName;

    @NotBlank(message = "El apellido paterno es obligatorio")
    @Size(max = 50, message = "El apellido paterno no debe exceder 50 caracteres")
    private String paternalLastName;

    @NotBlank(message = "El apellido materno es obligatorio")
    @Size(max = 50, message = "El apellido materno no debe exceder 50 caracteres")
    private String maternalLastName;

    @NotBlank(message = "El DNI es obligatorio")
    @Pattern(regexp = "\\d{8}", message = "El DNI debe tener 8 dígitos")
    private String dni;

    @NotBlank(message = "El celular es obligatorio")
    @Pattern(regexp = "\\d{9}", message = "El celular debe tener 9 dígitos")
    private String phoneNumber;

    @NotBlank(message = "El departamento es obligatorio")
    @Size(max = 40, message = "El departamento no debe exceder 40 caracteres")
    private String department;

    @NotBlank(message = "La provincia es obligatoria")
    @Size(max = 40, message = "La provincia no debe exceder 40 caracteres")
    private String province;

    @NotBlank(message = "El distrito es obligatorio")
    @Size(max = 60, message = "El distrito no debe exceder 60 caracteres")
    private String district;
}
