package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;
import java.util.GregorianCalendar;
import org.joda.time.Chronology;

/**
 * Test suite for {@link CalendarConverter}.
 */
public class CalendarConverterTest {

    /**
     * Tests that getInstantMillis() correctly extracts the millisecond
     * value from a given Calendar object.
     */
    @Test
    public void getInstantMillis_shouldReturnMillisFromCalendar() {
        // Arrange
        final long expectedMillis = 123456789L;
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(expectedMillis);

        CalendarConverter converter = CalendarConverter.INSTANCE;

        // Act
        // The Chronology argument is not used by this method, so passing null is acceptable.
        long actualMillis = converter.getInstantMillis(calendar, (Chronology) null);

        // Assert
        assertEquals(expectedMillis, actualMillis);
    }
}