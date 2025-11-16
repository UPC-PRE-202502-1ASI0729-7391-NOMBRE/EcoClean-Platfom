package pe.com.ecocleany.ecosmart.admin.application.specifications;

import org.springframework.data.jpa.domain.Specification;
import org.springframework.util.StringUtils;
import pe.com.ecocleany.ecosmart.admin.domain.model.Municipality;

public final class MunicipalitySpecifications {

    private MunicipalitySpecifications() {}

    public static Specification<Municipality> departmentEquals(String department) {
        if (!StringUtils.hasText(department)) {
            return null;
        }
        return (root, query, cb) -> cb.equal(cb.lower(root.get("department")), department.toLowerCase());
    }

    public static Specification<Municipality> provinceEquals(String province) {
        if (!StringUtils.hasText(province)) {
            return null;
        }
        return (root, query, cb) -> cb.equal(cb.lower(root.get("province")), province.toLowerCase());
    }

    public static Specification<Municipality> activeEquals(Boolean active) {
        if (active == null) {
            return null;
        }
        return (root, query, cb) -> cb.equal(root.get("active"), active);
    }
}
