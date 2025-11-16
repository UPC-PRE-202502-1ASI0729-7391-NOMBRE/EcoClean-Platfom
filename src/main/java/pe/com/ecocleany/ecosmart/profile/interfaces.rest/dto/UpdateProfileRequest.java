package pe.com.ecocleany.ecosmart.profile.interfaces.rest.dto;

import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.Data;

@Data
public class UpdateProfileRequest {

    @Size(max = 255, message = "La URL del avatar no debe exceder 255 caracteres")
    private String avatarUrl;

    @Size(max = 200, message = "La biografía no debe exceder 200 caracteres")
    private String bio;

    @Size(max = 120, message = "La ocupación no debe exceder 120 caracteres")
    private String occupation;

    @Size(max = 160, message = "La referencia de dirección no debe exceder 160 caracteres")
    private String addressReference;

    @Size(max = 120, message = "El contacto de emergencia no debe exceder 120 caracteres")
    private String emergencyContact;

    @Pattern(regexp = "(^$|\\d{9})", message = "El teléfono de emergencia debe contener 9 dígitos")
    private String emergencyPhone;
}
