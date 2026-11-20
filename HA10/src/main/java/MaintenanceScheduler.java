import java.time.*;
import java.time.zone.ZoneRules;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public final class MaintenanceScheduler {
    private final Clock clock;

    public MaintenanceScheduler(Clock clock) {
        this.clock = Objects.requireNonNull(clock, "clock cannot be null");
    }

    public MaintenanceWindow nextWindow(
            LocalDate date,
            LocalTime localStart,
            Duration duration,
            ZoneId zone,
            OverlapPolicy policy) {

        Objects.requireNonNull(date, "date cannot be null");
        Objects.requireNonNull(localStart, "localStart cannot be null");
        Objects.requireNonNull(duration, "duration cannot be null");
        Objects.requireNonNull(zone, "zone cannot be null");
        Objects.requireNonNull(policy, "policy cannot be null");

        if (duration.isZero() || duration.isNegative()) {
            throw new IllegalArgumentException("Duration must be positive");
        }

        LocalDateTime localDateTime = LocalDateTime.of(date, localStart);

        ZoneRules rules = zone.getRules();

        List<ZoneOffset> offsets = rules.getValidOffsets(localDateTime);

        if (offsets.isEmpty()) {
            throw new DateTimeException("Local time does not exist in this zone");
        }

        ZonedDateTime zonedDateTime = localDateTime.atZone(zone);

        if (offsets.size() > 1) {
            if (policy == OverlapPolicy.EARLIER) {
                zonedDateTime = zonedDateTime.withEarlierOffsetAtOverlap();
            } else {
                zonedDateTime = zonedDateTime.withLaterOffsetAtOverlap();
            }
        }

        Instant start = zonedDateTime.toInstant();
        Instant end = start.plus(duration);

        return new MaintenanceWindow(start, end);
    }

    public List<MaintenanceWindow> upcomingWindows(
            int days,
            LocalTime localStart,
            Duration duration,
            ZoneId zone,
            OverlapPolicy policy) {

        if (days < 0) {
            throw new IllegalArgumentException("Days cannot be negative");
        }

        Objects.requireNonNull(localStart, "localStart cannot be null");
        Objects.requireNonNull(duration, "duration cannot be null");
        Objects.requireNonNull(zone, "zone cannot be null");
        Objects.requireNonNull(policy, "policy cannot be null");

        List<MaintenanceWindow> windows = new ArrayList<>();

        LocalDate date = LocalDate.now(clock.withZone(zone));

        for (int i = 0; i < days; i++) {
            windows.add(nextWindow(
                    date.plusDays(i),
                    localStart,
                    duration,
                    zone,
                    policy
            ));
        }

        return windows;
    }
}