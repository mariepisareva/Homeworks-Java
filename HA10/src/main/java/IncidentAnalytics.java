import java.math.BigInteger;
import java.time.Duration;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

public class IncidentAnalytics {
    public static Duration totalDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents cannot be null");

        Duration total = Duration.ZERO;

        for (Incident incident : incidents) {
            total = total.plus(incident.duration());
        }

        return total;
    }

    public static Duration averageDowntime(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents cannot be null");

        if (incidents.isEmpty()) {
            return Duration.ZERO;
        }

        BigInteger totalNanos = BigInteger.ZERO;
        BigInteger billion = BigInteger.valueOf(1_000_000_000L);

        for (Incident incident : incidents) {
            Duration duration = incident.duration();

            BigInteger nanos = BigInteger.valueOf(duration.getSeconds())
                    .multiply(billion)
                    .add(BigInteger.valueOf(duration.getNano()));

            totalNanos = totalNanos.add(nanos);
        }

        BigInteger averageNanos = totalNanos.divide(
                BigInteger.valueOf(incidents.size())
        );

        BigInteger[] parts = averageNanos.divideAndRemainder(billion);

        return Duration.ofSeconds(
                parts[0].longValueExact(),
                parts[1].longValueExact()
        );
    }

    public static Optional<Incident> longestIncident(List<Incident> incidents) {
        Objects.requireNonNull(incidents, "incidents cannot be null");

        Incident longest = null;

        for (Incident incident : incidents) {
            if (longest == null) {
                longest = incident;
                continue;
            }

            int comparison = incident.duration().compareTo(longest.duration());

            if (comparison > 0 ||
                    (comparison == 0 &&
                            (incident.startedAt().isBefore(longest.startedAt()) ||
                                    (incident.startedAt().equals(longest.startedAt()) &&
                                            incident.id().compareTo(longest.id()) < 0)))) {
                longest = incident;
            }
        }

        return Optional.ofNullable(longest);
    }

    public static Map<LocalDate, List<Incident>> groupByStartDate(
            List<Incident> incidents, ZoneId zone) {

        Objects.requireNonNull(incidents, "incidents cannot be null");
        Objects.requireNonNull(zone, "zone cannot be null");

        Map<LocalDate, List<Incident>> result = new TreeMap<>();

        for (Incident incident : incidents) {
            LocalDate date = incident.startedAt()
                    .atZone(zone)
                    .toLocalDate();

            result.computeIfAbsent(date, key -> new ArrayList<>()).add(incident);
        }

        Comparator<Incident> comparator = Comparator
                .comparing(Incident::startedAt)
                .thenComparing(Incident::id);

        result.values().forEach(list -> list.sort(comparator));

        return result;
    }
}
