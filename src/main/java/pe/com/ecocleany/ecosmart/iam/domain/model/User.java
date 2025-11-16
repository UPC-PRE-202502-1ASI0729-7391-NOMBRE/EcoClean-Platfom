package pe.com.ecocleany.ecosmart.iam.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "iam_users")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @Column(nullable = false, unique = true, length = 120)
    private String email;

    @Column(nullable = false, length = 255)
    private String passwordHash;

    @Column(nullable = false, length = 80)
    private String firstName;

    @Column(nullable = false, length = 50)
    private String paternalLastName;

    @Column(nullable = false, length = 50)
    private String maternalLastName;

    @Column(nullable = false, unique = true, length = 8)
    private String dni;

    @Column(nullable = false, length = 15)
    private String phoneNumber;

    @Column(nullable = false, length = 40)
    private String department;

    @Column(nullable = false, length = 40)
    private String province;

    @Column(nullable = false, length = 60)
    private String district;

    @Column(name = "municipality_id")
    private UUID municipalityId;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.EAGER)
    @Builder.Default
    private Set<UserRole> roles = new HashSet<>();

    public void addRole(Role role) {
        UserRole userRole = new UserRole(this, role);
        roles.add(userRole);
    }

    public void removeRole(Role role) {
        roles.removeIf(userRole -> userRole.getRole().equals(role));
    }

    public boolean hasRole(RoleName roleName) {
        return roles.stream().anyMatch(userRole -> userRole.getRole().getName() == roleName);
    }

    public void updatePhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public void assignMunicipality(UUID municipalityId) {
        this.municipalityId = municipalityId;
    }
}
