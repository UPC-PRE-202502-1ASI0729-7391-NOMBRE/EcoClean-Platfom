package pe.com.ecocleany.ecosmart.operations.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Entity
@Table(name = "collection_routes")
@Getter
@Builder(toBuilder = true)
@NoArgsConstructor
@AllArgsConstructor
public class CollectionRoute {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, length = 60)
    private String name;

    @Column(nullable = false)
    private UUID municipalityId;

    @ElementCollection
    @CollectionTable(name = "collection_route_bins", joinColumns = @JoinColumn(name = "route_id"))
    @Column(name = "smartbin_id", nullable = false)
    private List<UUID> smartBinIds;

    @Column(nullable = false)
    private Instant scheduledAt;

    @Column
    private UUID operatorId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private RouteStatus status;

    public void assignOperator(UUID operatorId) {
        this.operatorId = operatorId;
        this.status = RouteStatus.ASSIGNED;
    }

    public void markInProgress() {
        this.status = RouteStatus.IN_PROGRESS;
    }

    public void markCompleted() {
        this.status = RouteStatus.COMPLETED;
    }

    public enum RouteStatus {
        PLANNED,
        ASSIGNED,
        IN_PROGRESS,
        COMPLETED
    }
}
