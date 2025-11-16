package pe.com.ecocleany.ecosmart.iam.application;

import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.iam.domain.model.User;
import pe.com.ecocleany.ecosmart.iam.infrastructure.repositories.UserRepository;

import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class IamUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        return new UserPrincipal(
                user.getId(),
                user.getEmail(),
                user.getPasswordHash(),
                user.getMunicipalityId(),
                user.getRoles()
                        .stream()
                        .map(userRole -> new SimpleGrantedAuthority("ROLE_" + userRole.getRole().getName().name()))
                        .collect(Collectors.toSet())
        );
    }
}
