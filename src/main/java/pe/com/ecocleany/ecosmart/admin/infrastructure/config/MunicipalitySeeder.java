package pe.com.ecocleany.ecosmart.admin.infrastructure.config;

import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;
import pe.com.ecocleany.ecosmart.admin.domain.model.Municipality;
import pe.com.ecocleany.ecosmart.admin.infrastructure.repositories.MunicipalityRepository;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class MunicipalitySeeder implements ApplicationRunner {

    private final MunicipalityRepository municipalityRepository;

    public MunicipalitySeeder(MunicipalityRepository municipalityRepository) {
        this.municipalityRepository = municipalityRepository;
    }

    @Override
    public void run(ApplicationArguments args) {
        if (municipalityRepository.count() == 0) {
            seedLimaAndCallao();
        }
    }

    private void seedLimaAndCallao() {
        Set<String> existing = new HashSet<>(municipalityRepository.findAll()
                .stream()
                .map(Municipality::getName)
                .toList());

        seedProvince(existing, "Lima", "Lima", List.of(
                "Ancón", "Ate", "Barranco", "Breña", "Carabayllo",
                "Chaclacayo", "Chorrillos", "Cieneguilla", "Comas", "El Agustino",
                "Independencia", "Jesús María", "La Molina", "La Victoria", "Lince",
                "Los Olivos", "Lurigancho", "Lurín", "Magdalena del Mar", "Miraflores",
                "Pachacámac", "Pucusana", "Pueblo Libre", "Puente Piedra", "Punta Hermosa",
                "Punta Negra", "Rímac", "San Bartolo", "San Borja", "San Isidro",
                "San Juan de Lurigancho", "San Juan de Miraflores", "San Luis",
                "San Martín de Porres", "San Miguel", "Santa Anita", "Santa María del Mar",
                "Santa Rosa", "Santiago de Surco", "Surquillo", "Villa El Salvador",
                "Villa María del Triunfo", "Cercado de Lima"));

        seedProvince(existing, "Callao", "Provincia Constitucional del Callao", List.of(
                "Callao", "Bellavista", "Carmen de La Legua-Reynoso", "La Perla",
                "La Punta", "Mi Perú", "Ventanilla"));
    }

    private void seedProvince(Set<String> existing, String department, String province, List<String> districts) {
        districts.stream()
                .filter(name -> !existing.contains(name))
                .map(name -> Municipality.builder()
                        .name(name)
                        .department(department)
                        .province(province)
                        .district(name)
                        .description(null)
                        .active(true)
                        .build())
                .forEach(municipalityRepository::save);
    }
}
