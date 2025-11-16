package pe.com.ecocleany.ecosmart.admin.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.UUID;

@Entity
@Table(name = "municipalities")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class Municipality {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, unique = true, length = 120)
    private String name;

    @Column(nullable = false, length = 60)
    private String department;

    @Column(nullable = false, length = 60)
    private String province;

    @Column(nullable = false, length = 60)
    private String district;

    @Column(length = 255)
    private String description;

    @Column(nullable = false)
    private boolean active;

    public void update(String name, String department, String province, String district, String description, boolean active) {
        this.name = name;
        this.department = department;
        this.province = province;
        this.district = district;
        this.description = description;
        this.active = active;
    }
}
