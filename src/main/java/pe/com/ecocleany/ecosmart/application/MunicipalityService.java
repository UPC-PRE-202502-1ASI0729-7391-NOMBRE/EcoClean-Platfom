package pe.com.ecocleany.ecosmart.admin.application;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;
import pe.com.ecocleany.ecosmart.admin.domain.model.Municipality;
import pe.com.ecocleany.ecosmart.admin.infrastructure.repositories.MunicipalityRepository;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.CreateMunicipalityRequest;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.MunicipalityResponse;
import pe.com.ecocleany.ecosmart.admin.interfaces.rest.dto.UpdateMunicipalityRequest;

import java.util.List;
import java.util.UUID;

import static pe.com.ecocleany.ecosmart.admin.application.specifications.MunicipalitySpecifications.activeEquals;
import static pe.com.ecocleany.ecosmart.admin.application.specifications.MunicipalitySpecifications.departmentEquals;
import static pe.com.ecocleany.ecosmart.admin.application.specifications.MunicipalitySpecifications.provinceEquals;

@Service
@RequiredArgsConstructor
@Transactional
public class    MunicipalityService {

    private final MunicipalityRepository municipalityRepository;
    private static final int MAX_PAGE_SIZE = 50;

    public MunicipalityResponse create(CreateMunicipalityRequest request) {
        if (municipalityRepository.existsByNameIgnoreCase(request.getName())) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Ya existe un municipio con ese nombre");
        }

        Municipality municipality = Municipality.builder()
                .name(request.getName())
                .department(request.getDepartment())
                .province(request.getProvince())
                .district(request.getDistrict())
                .description(request.getDescription())
                .active(true)
                .build();

        return mapToResponse(municipalityRepository.save(municipality));
    }

    @Transactional(readOnly = true)
    public pe.com.ecocleany.ecosmart.shared.interfaces.rest.dto.PageResponse<MunicipalityResponse> findAll(
            String department,
            String province,
            Boolean active,
            int page,
            int size
    ) {
        int resolvedSize = Math.min(Math.max(size, 1), MAX_PAGE_SIZE);
        Pageable pageable = PageRequest.of(Math.max(page, 0), resolvedSize, Sort.by("name").ascending());

        Specification<Municipality> specification = Specification.where(departmentEquals(department))
                .and(provinceEquals(province))
                .and(activeEquals(active));

        Page<Municipality> resultPage = municipalityRepository.findAll(specification, pageable);
        List<MunicipalityResponse> content = resultPage.getContent()
                .stream()
                .map(this::mapToResponse)
                .toList();

        return new pe.com.ecocleany.ecosmart.shared.interfaces.rest.dto.PageResponse<>(
                content,
                resultPage.getNumber(),
                resultPage.getSize(),
                resultPage.getTotalElements(),
                resultPage.getTotalPages(),
                resultPage.isLast()
        );
    }

    @Transactional(readOnly = true)
    public List<MunicipalityResponse> listActive(String department, String province) {
        Specification<Municipality> specification = Specification.where(departmentEquals(department))
                .and(provinceEquals(province))
                .and(activeEquals(true));
        return municipalityRepository.findAll(specification, Sort.by("name").ascending())
                .stream()
                .map(this::mapToResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public MunicipalityResponse findById(UUID id) {
        return mapToResponse(load(id));
    }

    public MunicipalityResponse update(UUID id, UpdateMunicipalityRequest request) {
        Municipality municipality = load(id);
        municipality.update(
                request.getName(),
                request.getDepartment(),
                request.getProvince(),
                request.getDistrict(),
                request.getDescription(),
                request.getActive()
        );
        return mapToResponse(municipalityRepository.save(municipality));
    }

    private Municipality load(UUID id) {
        return municipalityRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Municipio no encontrado"));
    }

    public Municipality ensureExists(UUID id) {
        return load(id);
    }

    private MunicipalityResponse mapToResponse(Municipality municipality) {
        return MunicipalityResponse.builder()
                .id(municipality.getId())
                .name(municipality.getName())
                .department(municipality.getDepartment())
                .province(municipality.getProvince())
                .district(municipality.getDistrict())
                .description(municipality.getDescription())
                .active(municipality.isActive())
                .build();
    }
}
