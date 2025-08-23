package org.joda.time.convert;

import org.joda.time.Chronology;
import org.junit.Test;

import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

import static org.junit.Assert.assertEquals;

/*
 * Note: The original test class name and scaffolding inheritance have been preserved
 * as requested. In a real-world scenario, this class would be renamed to
 * 'CalendarConverterTest' and would not rely on test-generation scaffolding.
 */
public class CalendarConverter_ESTestTest2 extends CalendarConverter_ESTest_scaffolding {

    @Test
    public void getInstantMillis_shouldReturnCorrectMillisecondsFromCalendar() {
        // Arrange
        CalendarConverter converter = new CalendarConverter();

        // Use a well-known date: the Unix epoch (1970-01-01T00:00:00.000Z).
        // This makes the expected result (0L) easy to understand and verify.
        Calendar epochCalendar = new GregorianCalendar(TimeZone.getTimeZone("UTC"));
        epochCalendar.setTimeInMillis(0L);

        long expectedMillis = 0L;

        // Act
        // The 'chronology' parameter is specified to be ignored by the getInstantMillis method,
        // so we pass null to confirm this behavior.
        long actualMillis = converter.getInstantMillis(epochCalendar, (Chronology) null);

        // Assert
        assertEquals("The converter should extract the exact millisecond instant from the Calendar.",
                expectedMillis, actualMillis);
    }
}