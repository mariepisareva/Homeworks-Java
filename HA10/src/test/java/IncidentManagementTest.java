import org.junit.jupiter.api.Test;

import java.time.*;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

class IncidentManagementTest {

    @Test
    void testIncidentParser() {
        Incident incident = IncidentParser.parse(
                "INC-1;Payment;2024-03-30T10:00:00Z;2024-03-30T12:30:00Z"
        );

        assertEquals("INC-1", incident.id());
        assertEquals("Payment", incident.service());
        assertEquals(Duration.ofMinutes(150), incident.duration());

        assertThrows(IllegalArgumentException.class, () ->
                IncidentParser.parse("INC-2;Payment;wrong-date;2024-03-30T12:00:00Z")
        );

        assertThrows(IllegalArgumentException.class, () ->
                IncidentParser.parse(";Payment;2024-03-30T10:00:00Z;2024-03-30T12:00:00Z")
        );

        assertThrows(IllegalArgumentException.class, () ->
                IncidentParser.parse("INC-3;Payment;2024-03-30T12:00:00Z;2024-03-30T10:00:00Z")
        );
    }

    @Test
    void testIncidentAnalytics() {
        Incident first = new Incident(
                "INC-1", "Payment",
                Instant.parse("2024-03-30T10:00:00Z"),
                Instant.parse("2024-03-30T10:00:00.000000001Z")
        );

        Incident second = new Incident(
                "INC-2", "Orders",
                Instant.parse("2024-03-30T11:00:00Z"),
                Instant.parse("2024-03-30T11:00:00.000000003Z")
        );

        List<Incident> incidents = List.of(first, second);

        assertEquals(Duration.ofNanos(4),
                IncidentAnalytics.totalDowntime(incidents));

        assertEquals(Duration.ofNanos(2),
                IncidentAnalytics.averageDowntime(incidents));

        assertEquals(
                Optional.of(second),
                IncidentAnalytics.longestIncident(incidents)
        );

        assertEquals(
                Duration.ZERO,
                IncidentAnalytics.totalDowntime(List.of())
        );

        assertEquals(
                Duration.ZERO,
                IncidentAnalytics.averageDowntime(List.of())
        );

        assertEquals(
                Optional.empty(),
                IncidentAnalytics.longestIncident(List.of())
        );
    }

    @Test
    void testGroupByStartDate() {
        Incident incident = new Incident(
                "INC-1", "Payment",
                Instant.parse("2024-03-30T23:30:00Z"),
                Instant.parse("2024-03-31T00:00:00Z")
        );

        ZoneId zone = ZoneId.of("Europe/Berlin");

        Map<LocalDate, List<Incident>> result =
                IncidentAnalytics.groupByStartDate(List.of(incident), zone);

        LocalDate expectedDate = LocalDate.of(2024, 3, 31);

        assertTrue(result.containsKey(expectedDate));
        assertEquals(List.of(incident), result.get(expectedDate));
    }

    @Test
    void testDaylightSavingGap() {
        MaintenanceScheduler scheduler =
                new MaintenanceScheduler(Clock.systemUTC());

        assertThrows(DateTimeException.class, () ->
                scheduler.nextWindow(
                        LocalDate.of(2024, 3, 31),
                        LocalTime.of(2, 30),
                        Duration.ofMinutes(30),
                        ZoneId.of("Europe/Berlin"),
                        OverlapPolicy.EARLIER
                )
        );
    }

    @Test
    void testDaylightSavingOverlap() {
        MaintenanceScheduler scheduler =
                new MaintenanceScheduler(Clock.systemUTC());

        LocalDate date = LocalDate.of(2024, 10, 27);
        LocalTime time = LocalTime.of(2, 30);
        ZoneId zone = ZoneId.of("Europe/Berlin");
        Duration duration = Duration.ofMinutes(30);

        MaintenanceWindow earlier = scheduler.nextWindow(
                date, time, duration, zone, OverlapPolicy.EARLIER
        );

        MaintenanceWindow later = scheduler.nextWindow(
                date, time, duration, zone, OverlapPolicy.LATER
        );

        assertEquals(
                ZoneOffset.ofHours(2),
                earlier.start().atZone(zone).getOffset()
        );

        assertEquals(
                ZoneOffset.ofHours(1),
                later.start().atZone(zone).getOffset()
        );

        assertEquals(
                Duration.ofHours(1),
                Duration.between(earlier.start(), later.start())
        );
    }

    @Test
    void testUpcomingWindows() {
        Clock clock = Clock.fixed(
                Instant.parse("2024-03-30T00:00:00Z"),
                ZoneOffset.UTC
        );

        MaintenanceScheduler scheduler = new MaintenanceScheduler(clock);

        ZoneId zone = ZoneId.of("Europe/Berlin");

        List<MaintenanceWindow> windows = scheduler.upcomingWindows(
                2,
                LocalTime.of(3, 30),
                Duration.ofMinutes(30),
                zone,
                OverlapPolicy.EARLIER
        );

        assertEquals(2, windows.size());

        assertEquals(
                LocalTime.of(3, 30),
                windows.get(0).start().atZone(zone).toLocalTime()
        );

        assertEquals(
                LocalTime.of(3, 30),
                windows.get(1).start().atZone(zone).toLocalTime()
        );

        assertEquals(
                Duration.ofHours(23),
                Duration.between(windows.get(0).start(), windows.get(1).start())
        );

        List<MaintenanceWindow> repeated = scheduler.upcomingWindows(
                2,
                LocalTime.of(3, 30),
                Duration.ofMinutes(30),
                zone,
                OverlapPolicy.EARLIER
        );

        assertEquals(windows, repeated);
    }

    @Test
    void testReportFormatterAndLegacyDateAdapter() {
        ZoneId zone = ZoneId.of("Europe/Berlin");

        MaintenanceWindow window = new MaintenanceWindow(
                Instant.parse("2024-03-31T01:00:00Z"),
                Instant.parse("2024-03-31T02:00:00Z")
        );

        String result = ReportFormatter.formatWindow(
                window, zone, Locale.GERMANY
        );

        assertEquals(
                "31.03.2024 03:00 +02:00 (Europe/Berlin) – " +
                        "31.03.2024 04:00 +02:00 (Europe/Berlin)",
                result
        );

        Instant instant = Instant.parse("2024-03-30T12:30:00Z");

        Date date = LegacyDateAdapter.toDate(instant);

        assertEquals(
                instant,
                LegacyDateAdapter.toInstant(date)
        );

        Date originalDate = new Date(1711800000000L);

        assertEquals(
                originalDate,
                LegacyDateAdapter.toDate(
                        LegacyDateAdapter.toInstant(originalDate)
                )
        );
    }
}