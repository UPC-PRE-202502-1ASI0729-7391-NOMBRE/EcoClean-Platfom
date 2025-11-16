package pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;

import java.util.UUID;

@Value
@Builder
public class MunicipalityResponse {
    UUID id;
    String name;
    String department;
    String province;
    String district;
    String description;
    boolean active;
}
