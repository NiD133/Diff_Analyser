package org.joda.time.convert;

import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import junit.framework.TestCase;
import org.joda.time.Chronology;
import org.joda.time.TimeOfDay;
import org.joda.time.chrono.ISOChronology;

/**
 * Unit tests for {@link CalendarConverter}.
 */
public class CalendarConverterTest extends TestCase {

    // A specific instant in time: 1970-01-01T03:25:45.678Z
    private static final long TEST_INSTANT_MILLIS = 12345678L;

    private Chronology isoChronology;

    @Override
    protected void setUp() throws Exception {
        isoChronology = ISOChronology.getInstance();
    }

    /**
     * Tests that getPartialValues correctly extracts time-of-day fields from a Calendar object.
     *
     * This test verifies that converting a Calendar to a partial time representation
     * yields the same result as creating the partial time directly from the
     * equivalent millisecond instant.
     */
    public void testGetPartialValues_fromCalendar_extractsCorrectTimeOfDay() {
        // Arrange
        // A TimeOfDay object is used as a template to specify which fields to extract.
        TimeOfDay timeOfDayPartial = new TimeOfDay();

        // Create a calendar and set it to a specific instant in time.
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(TEST_INSTANT_MILLIS);

        // The expected values are what the chronology would extract directly from the millis.
        int[] expectedPartialValues = isoChronology.get(timeOfDayPartial, TEST_INSTANT_MILLIS);

        // Act
        // The converter extracts the partial values from the Calendar object.
        int[] actualPartialValues = CalendarConverter.INSTANCE.getPartialValues(timeOfDayPartial, calendar, isoChronology);

        // Assert
        // Using Arrays.toString provides a much more informative failure message.
        assertEquals(Arrays.toString(expectedPartialValues), Arrays.toString(actualPartialValues));
    }
}