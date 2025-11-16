package pe.com.ecocleany.ecosmart.iam.application;

import org.springframework.stereotype.Component;
import pe.com.ecocleany.ecosmart.iam.domain.model.User;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.UserResponse;

import java.util.stream.Collectors;

@Component
public class UserMapper {

    public UserResponse toResponse(User user) {
        return UserResponse.builder()
                .id(user.getId())
                .email(user.getEmail())
                .firstName(user.getFirstName())
                .paternalLastName(user.getPaternalLastName())
                .maternalLastName(user.getMaternalLastName())
                .dni(user.getDni())
                .phoneNumber(user.getPhoneNumber())
                .department(user.getDepartment())
                .province(user.getProvince())
                .district(user.getDistrict())
                .municipalityId(user.getMunicipalityId())
                .roles(user.getRoles()
                        .stream()
                        .map(userRole -> userRole.getRole().getName())
                        .collect(Collectors.toSet()))
                .build();
    }
}
