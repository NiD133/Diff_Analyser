package org.joda.time.chrono;

import org.joda.time.Chronology;
import org.joda.time.DateTime;
import org.joda.time.DateTimeUtils;
import org.joda.time.DateTimeZone;
import org.joda.time.DurationField;
import org.joda.time.DurationFieldType;
import org.joda.time.YearMonthDay;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Locale;
import java.util.TimeZone;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

/**
 * Tests for {@link ISOChronology}.
 * This test suite focuses on the factory methods and basic properties of the ISOChronology.
 */
public class ISOChronologyTest {

    // --- Constants ---

    private static final DateTimeZone PARIS = DateTimeZone.forID("Europe/Paris");
    private static final DateTimeZone LONDON = DateTimeZone.forID("Europe/London");
    private static final DateTimeZone TOKYO = DateTimeZone.forID("Asia/Tokyo");

    // A fixed point in time for consistent test results: 2002-06-09T00:00:00Z
    private static final long TEST_TIME_NOW = new DateTime(2002, 6, 9, 0, 0, 0, 0, DateTimeZone.UTC).getMillis();

    // --- State Management ---

    private DateTimeZone originalDateTimeZone;
    private TimeZone originalTimeZone;
    private Locale originalLocale;

    // --- Test Setup and Teardown ---

    @BeforeEach
    public void setUp() {
        // Fix the current time to a known value for predictable test execution.
        DateTimeUtils.setCurrentMillisFixed(TEST_TIME_NOW);

        // Set up a default environment for tests.
        // Note: Modifying global JVM state like default time zone and locale is risky
        // and can lead to flaky tests. It's done here to match the original test's behavior.
        originalDateTimeZone = DateTimeZone.getDefault();
        originalTimeZone = TimeZone.getDefault();
        originalLocale = Locale.getDefault();
        DateTimeZone.setDefault(LONDON);
        TimeZone.setDefault(TimeZone.getTimeZone("Europe/London"));
        Locale.setDefault(Locale.UK);
    }

    @AfterEach
    public void tearDown() {
        // Restore the original system state to avoid side effects on other tests.
        DateTimeUtils.setCurrentMillisSystem();
        DateTimeZone.setDefault(originalDateTimeZone);
        TimeZone.setDefault(originalTimeZone);
        Locale.setDefault(originalLocale);
    }

    // --- Test Cases ---

    @Test
    public void getInstanceUTC_shouldReturnUTCChronologyInstance() {
        // The getInstanceUTC() factory method should return a singleton instance
        // of ISOChronology configured for the UTC time zone.

        // When
        ISOChronology chronology = ISOChronology.getInstanceUTC();

        // Then
        assertEquals(DateTimeZone.UTC, chronology.getZone(), "Chronology should have UTC zone");
        assertSame(ISOChronology.class, chronology.getClass(), "The returned object should be of the exact ISOChronology class");
    }

    // --- Helper Methods ---

    /**
     * A helper method to assert that adding a duration field to a start date
     * correctly results in an expected end date. This method is not used in this file
     * but is preserved from the original test suite for completeness.
     *
     * @param start The starting date string in ISO format.
     * @param type  The type of the duration field to add (e.g., years, months).
     * @param amt   The amount of the duration field to add.
     * @param end   The expected end date string in ISO format.
     */
    private void assertChronologyAddsDurationFieldCorrectly(String start, DurationFieldType type, int amt, String end) {
        DateTime dtStart = new DateTime(start, ISOChronology.getInstanceUTC());
        DateTime dtEnd = new DateTime(end, ISOChronology.getInstanceUTC());

        // Test forward addition
        assertEquals(dtEnd, dtStart.withFieldAdded(type, amt));

        // Test backward addition
        assertEquals(dtStart, dtEnd.withFieldAdded(type, -amt));

        // Test difference calculation
        DurationField field = type.getField(ISOChronology.getInstanceUTC());
        int diff = field.getDifference(dtEnd.getMillis(), dtStart.getMillis());
        assertEquals(amt, diff);

        // Test with YearMonthDay for date-based fields
        if (type == DurationFieldType.years() || type == DurationFieldType.months() || type == DurationFieldType.days()) {
            YearMonthDay ymdStart = new YearMonthDay(start, ISOChronology.getInstanceUTC());
            YearMonthDay ymdEnd = new YearMonthDay(end, ISOChronology.getInstanceUTC());
            assertEquals(ymdEnd, ymdStart.withFieldAdded(type, amt));
            assertEquals(ymdStart, ymdEnd.withFieldAdded(type, -amt));
        }
    }
}