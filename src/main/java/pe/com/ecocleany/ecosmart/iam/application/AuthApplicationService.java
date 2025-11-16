package pe.com.ecocleany.ecosmart.iam.application;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.iam.domain.model.Role;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;
import pe.com.ecocleany.ecosmart.iam.domain.model.User;
import pe.com.ecocleany.ecosmart.iam.infrastructure.adapters.JwtTokenProvider;
import pe.com.ecocleany.ecosmart.iam.infrastructure.repositories.RoleRepository;
import pe.com.ecocleany.ecosmart.iam.infrastructure.repositories.UserRepository;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.AuthResponse;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.LoginRequest;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.RegisterUserRequest;
import pe.com.ecocleany.ecosmart.iam.interfaces.rest.dto.UserResponse;
import pe.com.ecocleany.ecosmart.shared.application.ProfileInitializer;

import java.util.Set;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@Transactional
@RequiredArgsConstructor
public class AuthApplicationService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtTokenProvider jwtTokenProvider;
    private final UserMapper userMapper;
    private final ProfileInitializer profileInitializer;

    public AuthResponse register(RegisterUserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El correo electrónico ya está registrado");
        }
        if (userRepository.existsByDni(request.getDni())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El DNI ya está registrado");
        }

        Role citizenRole = roleRepository.findByName(RoleName.CITIZEN)
                .orElseThrow(() -> new IllegalStateException("Debe existir el rol CITIZEN antes de registrar usuarios"));

        User user = User.builder()
                .email(request.getEmail())
                .passwordHash(passwordEncoder.encode(request.getPassword()))
                .firstName(request.getFirstName())
                .paternalLastName(request.getPaternalLastName())
                .maternalLastName(request.getMaternalLastName())
                .dni(request.getDni())
                .phoneNumber(request.getPhoneNumber())
                .department(request.getDepartment())
                .province(request.getProvince())
                .district(request.getDistrict())
                .build();

        user.addRole(citizenRole);

        User saved = userRepository.save(user);
        profileInitializer.ensureProfile(saved.getId());

        return buildAuthResponse(saved);
    }

    public AuthResponse login(LoginRequest request) {
        User user = userRepository.findByEmail(request.getEmail())
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas"));

        if (!passwordEncoder.matches(request.getPassword(), user.getPasswordHash())) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "Credenciales inválidas");
        }

        return buildAuthResponse(user);
    }

    @Transactional(readOnly = true)
    public UserResponse me(UUID userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Usuario no encontrado"));
        return userMapper.toResponse(user);
    }

    private AuthResponse buildAuthResponse(User user) {
        Set<RoleName> roleNames = user.getRoles()
                .stream()
                .map(userRole -> userRole.getRole().getName())
                .collect(Collectors.toSet());

        JwtTokenProvider.TokenPair tokenPair = jwtTokenProvider.generateToken(user, roleNames);

        return AuthResponse.builder()
                .token(tokenPair.token())
                .expiresAt(tokenPair.expiresAt())
                .user(userMapper.toResponse(user))
                .build();
    }
}
