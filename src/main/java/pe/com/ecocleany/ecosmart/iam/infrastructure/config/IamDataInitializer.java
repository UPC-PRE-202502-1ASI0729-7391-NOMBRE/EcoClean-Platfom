package pe.com.ecocleany.ecosmart.iam.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import pe.com.ecocleany.ecosmart.iam.domain.model.Role;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;
import pe.com.ecocleany.ecosmart.iam.domain.model.User;
import pe.com.ecocleany.ecosmart.iam.infrastructure.repositories.RoleRepository;
import pe.com.ecocleany.ecosmart.iam.infrastructure.repositories.UserRepository;

import java.util.Arrays;

@Component
@RequiredArgsConstructor
public class IamDataInitializer implements ApplicationRunner {

    private final RoleRepository roleRepository;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Value("${security.admin.email:admin@ecosmart.pe}")
    private String adminEmail;

    @Value("${security.admin.password:Admin123!}")
    private String adminPassword;

    @Value("${security.admin.dni:00000000}")
    private String adminDni;

    @Override
    @Transactional
    public void run(ApplicationArguments args) {
        ensureRoles();
        ensureAdminUser();
    }

    private void ensureRoles() {
        Arrays.stream(RoleName.values())
                .forEach(roleName -> roleRepository.findByName(roleName)
                        .orElseGet(() -> roleRepository.save(Role.builder()
                                .name(roleName)
                                .description(roleName.name().replace("_", " "))
                                .build())));
    }

    private void ensureAdminUser() {
        userRepository.findByEmail(adminEmail).ifPresentOrElse(user -> {
            boolean hasAdmin = user.getRoles().stream()
                    .anyMatch(userRole -> userRole.getRole().getName() == RoleName.ADMIN);
            if (!hasAdmin) {
                Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                        .orElseThrow();
                user.addRole(adminRole);
                userRepository.save(user);
            }
        }, () -> {
            Role adminRole = roleRepository.findByName(RoleName.ADMIN)
                    .orElseThrow();
            User admin = User.builder()
                    .email(adminEmail)
                    .passwordHash(passwordEncoder.encode(adminPassword))
                    .firstName("Administrador")
                    .paternalLastName("EcoSmart")
                    .maternalLastName("Root")
                    .dni(adminDni)
                    .phoneNumber("999999999")
                    .department("Lima")
                    .province("Lima")
                    .district("Miraflores")
                    .build();
            admin.addRole(adminRole);
            userRepository.save(admin);
        });
    }
}
