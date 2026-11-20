import java.time.Duration;
import java.time.Instant;
import java.util.Objects;

public record MaintenanceWindow(Instant start, Instant end) {
    public MaintenanceWindow {
        Objects.requireNonNull(start, "start cannot be null");
        Objects.requireNonNull(end, "end cannot be null");

        if (end.isBefore(start)) {
            throw new IllegalArgumentException("End cannot be before start");
        }
    }

    public Duration duration() {
        return Duration.between(start, end);
    }
}