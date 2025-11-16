package pe.com.ecocleany.ecosmart.smartbins.interfaces.rest.dto;

import jakarta.validation.constraints.*;
import lombok.Data;

import java.math.BigDecimal;
import java.util.UUID;

@Data
public class CreateSmartBinRequest {

    @NotBlank(message = "El código de tacho es obligatorio")
    private String binCode;

    @NotNull(message = "El municipio es obligatorio")
    private UUID municipalityId;

    @NotBlank(message = "El distrito es obligatorio")
    private String district;

    @NotNull(message = "La latitud es obligatoria")
    @DecimalMin(value = "-90.0", message = "La latitud mínima es -90")
    @DecimalMax(value = "90.0", message = "La latitud máxima es 90")
    private BigDecimal latitude;

    @NotNull(message = "La longitud es obligatoria")
    @DecimalMin(value = "-180.0", message = "La longitud mínima es -180")
    @DecimalMax(value = "180.0", message = "La longitud máxima es 180")
    private BigDecimal longitude;

    @NotBlank(message = "La dirección es obligatoria")
    private String address;

    @NotNull(message = "La capacidad en litros es obligatoria")
    @Min(value = 40, message = "La capacidad mínima es 40L")
    private Integer capacityLiters;
}
