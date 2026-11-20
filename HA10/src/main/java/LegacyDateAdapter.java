import java.time.Instant;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Objects;

public class LegacyDateAdapter {

    public static Instant toInstant(Date date) {
        Objects.requireNonNull(date, "date cannot be null");
        return date.toInstant();
    }

    public static Date toDate(Instant instant) {
        Objects.requireNonNull(instant, "instant cannot be null");
        return Date.from(instant);
    }

    public static ZonedDateTime toZonedDateTime(GregorianCalendar calendar) {
        Objects.requireNonNull(calendar, "calendar cannot be null");
        return calendar.toZonedDateTime();
    }

    public static GregorianCalendar toGregorianCalendar(ZonedDateTime dateTime) {
        Objects.requireNonNull(dateTime, "dateTime cannot be null");
        return GregorianCalendar.from(dateTime);
    }
}