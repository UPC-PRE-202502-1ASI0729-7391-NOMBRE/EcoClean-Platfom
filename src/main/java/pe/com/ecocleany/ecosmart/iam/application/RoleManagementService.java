package pe.com.ecocleany.ecosmart.iam.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.iam.domain.model.Role;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;
import pe.com.ecocleany.ecosmart.iam.domain.model.User;
import pe.com.ecocleany.ecosmart.iam.infrastructure.repositories.RoleRepository;
import pe.com.ecocleany.ecosmart.iam.infrastructure.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.UserResponse;

import org.springframework.util.StringUtils;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Transactional
public class RoleManagementService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final UserMapper userMapper;

    public UserResponse assignRole(UUID userId, RoleName roleName) {
        User user = findUser(userId);
        Role role = findRole(roleName);

        if (user.hasRole(roleName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya tiene el rol indicado");
        }

        user.addRole(role);
        return userMapper.toResponse(userRepository.save(user));
    }

    public UserResponse removeRole(UUID userId, RoleName roleName) {
        User user = findUser(userId);
        Role role = findRole(roleName);

        if (!user.hasRole(roleName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario no tiene el rol indicado");
        }

        user.removeRole(role);
        return userMapper.toResponse(userRepository.save(user));
    }

    public void updatePhoneNumber(UUID userId, String phoneNumber) {
        if (!StringUtils.hasText(phoneNumber)) {
            return;
        }
        User user = findUser(userId);
        user.updatePhoneNumber(phoneNumber);
        userRepository.save(user);
    }

    public void assignMunicipality(UUID userId, UUID municipalityId) {
        setMunicipality(userId, municipalityId, false);
    }

    public UserResponse forceAssignMunicipality(UUID userId, UUID municipalityId) {
        return setMunicipality(userId, municipalityId, true);
    }

    public UUID getUserMunicipality(UUID userId) {
        return findUser(userId).getMunicipalityId();
    }

    private UserResponse setMunicipality(UUID userId, UUID municipalityId, boolean allowOverride) {
        if (municipalityId == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El municipio es obligatorio");
        }
        User user = findUser(userId);
        if (!allowOverride && user.getMunicipalityId() != null && !user.getMunicipalityId().equals(municipalityId)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El usuario ya está asignado a otro municipio");
        }
        user.assignMunicipality(municipalityId);
        return userMapper.toResponse(userRepository.save(user));
    }

    private User findUser(UUID userId) {
        return userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
    }

    private Role findRole(RoleName roleName) {
        return roleRepository.findByName(roleName)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol no encontrado"));
    }
}
