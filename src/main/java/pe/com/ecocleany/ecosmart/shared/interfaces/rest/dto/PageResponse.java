package pe.com.ecocleany.ecosmart.shared.interfaces.rest.dto;

import java.util.List;

public record PageResponse<T>(
        List<T> items,
        int page,
        int size,
        long totalElements,
        int totalPages,
        boolean last
) {
}
