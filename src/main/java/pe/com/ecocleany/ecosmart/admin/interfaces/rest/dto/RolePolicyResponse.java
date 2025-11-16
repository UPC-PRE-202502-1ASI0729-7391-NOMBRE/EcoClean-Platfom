package pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto;

import lombok.Builder;
import lombok.Value;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

import java.util.List;

@Value
@Builder
public class RolePolicyResponse {
    RoleName role;
    String description;
    List<String> capabilities;
    boolean assignable;
}
