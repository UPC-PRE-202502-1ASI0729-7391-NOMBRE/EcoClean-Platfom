package pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class LoginRequest {

    @Email(message = "El correo electrónico no tiene un formato válido")
    @NotBlank(message = "El correo electrónico es obligatorio")
    private String email;

    @NotBlank(message = "La contraseña es obligatoria")
    @Schema(example = "string")
    private String password;
}
