package pe.com.ecocleany.ecosmart.admin.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.RoleAssignmentResponse;
import pe.com.ecocleany.ecosmart.iam.domain.model.Role;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;
import pe.com.ecocleany.ecosmart.iam.domain.model.User;
import pe.com.ecocleany.ecosmart.iam.infrastructure.repositories.RoleRepository;
import pe.com.ecocleany.ecosmart.iam.infrastructure.repositories.UserRepository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class RoleAssignmentService {

    private static final List<RoleName> MANAGED_ROLES = List.of(
            RoleName.ADMIN,
            RoleName.MUNICIPAL_OFFICER,
            RoleName.OPERATOR,
            RoleName.CITIZEN
    );

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public List<RoleAssignmentResponse> listAssignments() {
        return userRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(User::getFirstName))
                .map(this::toResponse)
                .toList();
    }

    public RoleAssignmentResponse updateRole(UUID userId, RoleName roleName) {
        if (!MANAGED_ROLES.contains(roleName)) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Rol no soportado");
        }
        User user = findUser(userId);

        MANAGED_ROLES.stream()
                .filter(existing -> existing != roleName && user.hasRole(existing))
                .forEach(existing -> user.removeRole(findRole(existing)));

        if (!user.hasRole(roleName)) {
            user.addRole(findRole(roleName));
        }

        User saved = userRepository.save(user);
        return toResponse(saved);
    }

    private RoleAssignmentResponse toResponse(User user) {
        String fullname = user.getFirstName() + " " + user.getPaternalLastName();
        RoleName effectiveRole = resolveEffectiveRole(user);
        return RoleAssignmentResponse.builder()
                .id(user.getId())
                .name(fullname.trim())
                .email(user.getEmail())
                .role(effectiveRole)
                .availableRoles(MANAGED_ROLES)
                .build();
    }

    private RoleName resolveEffectiveRole(User user) {
        if (user.hasRole(RoleName.ADMIN)) {
            return RoleName.ADMIN;
        }
        if (user.hasRole(RoleName.MUNICIPAL_OFFICER)) {
            return RoleName.MUNICIPAL_OFFICER;
        }
        if (user.hasRole(RoleName.OPERATOR)) {
            return RoleName.OPERATOR;
        }
        return RoleName.CITIZEN;
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
