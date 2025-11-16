package pe.com.ecocleany.ecosmart.iam.infrastructure.config;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import pe.com.ecocleany.ecosmart.iam.application.IamUserDetailsService;
import pe.com.ecocleany.ecosmart.iam.infrastructure.adapters.JwtTokenProvider;
import pe.com.ecocleany.ecosmart.iam.infrastructure.adapters.filters.JwtAuthenticationFilter;

import java.util.List;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig {

    private final JwtTokenProvider jwtTokenProvider;
    private final IamUserDetailsService userDetailsService;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration configuration) throws Exception {
        return configuration.getAuthenticationManager();
    }

    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        return new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http,
                                                   JwtAuthenticationFilter authenticationFilter) throws Exception {
        http.cors(Customizer.withDefaults())
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers(
                                "/api/iam/auth/register",
                                "/api/iam/auth/login",
                                "/actuator/health",
                                "/swagger-ui/**",
                                "/v3/api-docs/**"
                        ).permitAll()
                        .requestMatchers(HttpMethod.POST, "/api/admin/role-applications").authenticated()
                        .requestMatchers("/api/admin/role-applications/**").hasAnyRole("ADMIN", "MUNICIPAL_OFFICER")
                        .requestMatchers("/api/admin/municipalities/**").hasRole("ADMIN")
                        .requestMatchers(HttpMethod.GET, "/api/municipalities/**").authenticated()
                        .requestMatchers(HttpMethod.POST, "/api/smartbins").hasAnyRole("ADMIN", "MUNICIPAL_OFFICER")
                        .requestMatchers(HttpMethod.POST, "/api/smartbins/*/empty").hasAnyRole("ADMIN", "MUNICIPAL_OFFICER", "OPERATOR")
                        .requestMatchers("/api/monitoring/**").hasAnyRole("ADMIN", "MUNICIPAL_OFFICER")
                        .requestMatchers(HttpMethod.POST, "/api/operations/routes/*/bins/**").hasAnyRole("ADMIN", "MUNICIPAL_OFFICER", "OPERATOR")
                        .requestMatchers(HttpMethod.POST, "/api/operations/routes/**").hasAnyRole("ADMIN", "MUNICIPAL_OFFICER")
                        .requestMatchers(HttpMethod.GET, "/api/operations/routes/me").hasAnyRole("OPERATOR", "ADMIN", "MUNICIPAL_OFFICER")
                        .requestMatchers(HttpMethod.GET, "/api/operations/routes").hasAnyRole("ADMIN", "MUNICIPAL_OFFICER")
                        .requestMatchers("/api/admin/**", "/api/profile/admin/**").hasRole("ADMIN")
                        .anyRequest().authenticated())
                .addFilterBefore(authenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}
