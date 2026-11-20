import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record Incident(
        String id,
        String service,
        Instant startedAt,
        Instant resolvedAt) {
    public Incident {
        Objects.requireNonNull(id, "id cannot be null");
        Objects.requireNonNull(service, "service name cannot be null");
        Objects.requireNonNull(startedAt, "startedAt cannot be null");
        Objects.requireNonNull(resolvedAt, "resolvedAt cannot be null");

        if (resolvedAt.isBefore(startedAt)) {
            throw new IllegalArgumentException("resolvedAt cannot be earlier startedAt");
        }

        if (id.isBlank()) {
            throw new IllegalArgumentException("id cannot be blank");
        }

        if (service.isBlank()) {
            throw new IllegalArgumentException("service name cannot be blank");
        }
    }
    public Duration duration() {
        return Duration.between(startedAt, resolvedAt);
    }


}