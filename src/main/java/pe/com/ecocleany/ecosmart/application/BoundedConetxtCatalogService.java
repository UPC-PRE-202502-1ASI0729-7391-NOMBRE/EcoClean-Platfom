package pe.com.ecocleany.ecosmart.admin.application;

import org.springframework.stereotype.Service;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.BoundedContextResponse;
import pe.com.ecocleany.ecosmart.iam.domain.model.RoleName;

import java.util.List;

@Service
public class BoundedContextCatalogService {

    private static final List<BoundedContextResponse> CONTEXTS = List.of(
            BoundedContextResponse.builder()
                    .key("access")
                    .name("Acceso y seguridad")
                    .description("Registro, autenticación y gestión de sesiones seguras para habilitar al resto de módulos.")
                    .targetRoute("/panel")
                    .status("ready")
                    .roles(List.of(RoleName.OPERATOR, RoleName.MUNICIPAL_OFFICER, RoleName.ADMIN))
                    .build(),
            BoundedContextResponse.builder()
                    .key("profile")
                    .name("Perfiles ciudadanos")
                    .description("Gestión del perfil del ciudadano y contactos de referencia.")
                    .targetRoute("/profile")
                    .status("ready")
                    .roles(List.of(RoleName.CITIZEN, RoleName.OPERATOR, RoleName.MUNICIPAL_OFFICER, RoleName.ADMIN))
                    .build(),
            BoundedContextResponse.builder()
                    .key("monitoring")
                    .name("Monitoreo urbano")
                    .description("Panel de estado de tachos inteligentes filtrados por municipio.")
                    .targetRoute("/monitoring")
                    .status("pending")
                    .roles(List.of(RoleName.MUNICIPAL_OFFICER, RoleName.ADMIN))
                    .build(),
            BoundedContextResponse.builder()
                    .key("smartbins")
                    .name("Tachos inteligentes")
                    .description("Registro y gestión de tachos inteligentes y operaciones asociadas.")
                    .targetRoute("/smartbins")
                    .status("pending")
                    .roles(List.of(RoleName.MUNICIPAL_OFFICER, RoleName.ADMIN))
                    .build(),
            BoundedContextResponse.builder()
                    .key("operations")
                    .name("Operaciones")
                    .description("Planificación de rutas de recolección y asignación de operadores.")
                    .targetRoute("/operations")
                    .status("pending")
                    .roles(List.of(RoleName.OPERATOR, RoleName.MUNICIPAL_OFFICER, RoleName.ADMIN))
                    .build(),
            BoundedContextResponse.builder()
                    .key("admin")
                    .name("Administración")
                    .description("Gestión de municipios, roles y aprobaciones administrativas.")
                    .targetRoute("/admin")
                    .status("ready")
                    .roles(List.of(RoleName.ADMIN))
                    .build()
    );

    public List<BoundedContextResponse> listAll() {
        return CONTEXTS;
    }
}
