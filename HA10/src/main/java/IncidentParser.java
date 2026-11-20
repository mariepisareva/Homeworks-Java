import java.time.Instant;
import java.time.format.DateTimeParseException;

public class IncidentParser {
    public static Incident parse(String line) {
        if (line == null) {
            throw new IllegalArgumentException("line cannot be null");
        }

        String[] parsedLine = line.split(";", -1);

        if (parsedLine.length < 4) {
            throw new IllegalArgumentException("not enough arguements: needs to be 4 fields");
        }

        for (String field : parsedLine) {
            if (field.isBlank()) {
                throw new IllegalArgumentException(
                        "fields cannot be blank"
                );
            }
        }

        Instant startedAt;
        Instant resolvedAt;

        try {
            startedAt = Instant.parse(parsedLine[2]);
            resolvedAt = Instant.parse(parsedLine[3]);
        } catch (DateTimeParseException e) {
            throw new IllegalArgumentException(
                    "invalid date format", e
            );
        }

        return new Incident(
                parsedLine[0],
                parsedLine[1],
                startedAt,
                resolvedAt
        );
    }
}
