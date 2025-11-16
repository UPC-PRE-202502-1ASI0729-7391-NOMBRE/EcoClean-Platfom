package pe.com.ecocleany.ecosmart.admin.application;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.RolePolicyResponse;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

import java.util.List;

@Service
public class RolePolicyCatalogService {

    private static final List<RolePolicyResponse> POLICIES = List.of(
            RolePolicyResponse.builder()
                    .role(RoleName.ADMIN)
                    .description("Supervisa toda la plataforma EcoSmart y habilita los módulos críticos.")
                    .capabilities(List.of(
                            "Gestiona municipios y funcionarios",
                            "Aprueba postulaciones de roles",
                            "Accede a reportes operativos globales"
                    ))
                    .assignable(true)
                    .build(),
            RolePolicyResponse.builder()
                    .role(RoleName.MUNICIPAL_OFFICER)
                    .description("Coordina a nivel municipal la operación y monitoreo urbano.")
                    .capabilities(List.of(
                            "Gestiona tachos inteligentes del municipio",
                            "Aprueba operadores y rutas",
                            "Monitorea alertas y capacidad de recolección"
                    ))
                    .assignable(true)
                    .build(),
            RolePolicyResponse.builder()
                    .role(RoleName.OPERATOR)
                    .description("Ejecución en campo de las rutas y tareas de recolección.")
                    .capabilities(List.of(
                            "Recibe rutas asignadas",
                            "Reporta incidencias desde la app",
                            "Actualiza el estado de los tachos recolectados"
                    ))
                    .assignable(true)
                    .build(),
            RolePolicyResponse.builder()
                    .role(RoleName.CITIZEN)
                    .description("Acceso ciudadano para seguimiento de su perfil y notificaciones.")
                    .capabilities(List.of(
                            "Actualiza su información personal",
                            "Envía solicitudes de soporte",
                            "Consulta el estado de sus incidencias"
                    ))
                    .assignable(false)
                    .build()
    );

    public List<RolePolicyResponse> listAll() {
        return POLICIES;
    }
}
