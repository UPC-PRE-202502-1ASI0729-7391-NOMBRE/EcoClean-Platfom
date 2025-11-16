package pe.com.ecocleany.ecosmart.operations.domain.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.Instant;
import java.util.UUID;

@Entity
@Table(name = "collection_route_checkpoints",
        uniqueConstraints = @UniqueConstraint(columnNames = {"route_id", "smart_bin_id"}))
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class CollectionRouteCheckpoint {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "route_id")
    private CollectionRoute route;

    @Column(name = "smart_bin_id", nullable = false)
    private UUID smartBinId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private CheckpointStatus status;

    @Column
    private Instant visitedAt;

    @Column
    private UUID confirmedBy;

    public void markCompleted(UUID operatorId) {
        this.status = CheckpointStatus.COMPLETED;
        this.visitedAt = Instant.now();
        this.confirmedBy = operatorId;
    }

    public enum CheckpointStatus {
        PENDING,
        COMPLETED
    }
}
