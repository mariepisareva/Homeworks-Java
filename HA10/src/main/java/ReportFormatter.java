import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Locale;
import java.util.Objects;

public class ReportFormatter {
    public static String formatWindow(
            MaintenanceWindow window,
            ZoneId zone,
            Locale locale) {

        Objects.requireNonNull(window, "window cannot be null");
        Objects.requireNonNull(zone, "zone cannot be null");
        Objects.requireNonNull(locale, "locale cannot be null");

        DateTimeFormatter formatter = DateTimeFormatter
                .ofPattern("dd.MM.uuuu HH:mm XXX (VV)", locale);

        String start = window.start()
                .atZone(zone)
                .format(formatter);

        String end = window.end()
                .atZone(zone)
                .format(formatter);

        return start + " – " + end;
    }
}