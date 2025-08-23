package org.joda.time.convert;

import org.joda.time.Chronology;
import org.junit.Test;

/**
 * Unit tests for {@link CalendarConverter}.
 */
public class CalendarConverterTest {

    /**
     * Tests that getInstantMillis() throws a ClassCastException when the input object
     * is not an instance of java.util.Calendar.
     */
    @Test(expected = ClassCastException.class)
    public void getInstantMillis_whenObjectIsNotCalendar_shouldThrowClassCastException() {
        // Arrange
        CalendarConverter converter = CalendarConverter.INSTANCE;
        Object invalidObject = new Object();

        // Act & Assert
        // This call is expected to throw a ClassCastException because the converter
        // cannot handle a plain Object.
        converter.getInstantMillis(invalidObject, (Chronology) null);
    }
}