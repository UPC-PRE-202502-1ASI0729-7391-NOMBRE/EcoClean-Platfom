package pe.com.ecocleany.ecosmart.profile.domain.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "profiles")
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Profile {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(name = "user_id", nullable = false, unique = true)
    private UUID userId;

    @Column(length = 255)
    private String avatarUrl;

    @Column(length = 200)
    private String bio;

    @Column(length = 120)
    private String occupation;

    @Column(length = 160)
    private String addressReference;

    @Column(length = 120)
    private String emergencyContact;

    @Column(length = 15)
    private String emergencyPhone;

    @Column(nullable = false)
    private Instant updatedAt;

    public static Profile create(UUID userId) {
        return Profile.builder()
                .userId(userId)
                .updatedAt(Instant.now())
                .build();
    }

    public void updateDetails(String avatarUrl,
                              String bio,
                              String occupation,
                              String addressReference,
                              String emergencyContact,
                              String emergencyPhone) {
        this.avatarUrl = avatarUrl;
        this.bio = bio;
        this.occupation = occupation;
        this.addressReference = addressReference;
        this.emergencyContact = emergencyContact;
        this.emergencyPhone = emergencyPhone;
        this.updatedAt = Instant.now();
    }

    @PrePersist
    @PreUpdate
    void onPersist() {
        this.updatedAt = Instant.now();
    }
}
