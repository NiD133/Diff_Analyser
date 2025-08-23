package org.joda.time.convert;

import java.util.Calendar;
import junit.framework.TestCase;

/**
 * Unit tests for {@link CalendarConverter}.
 */
public class CalendarConverterTest extends TestCase {

    /**
     * Tests that getSupportedType() correctly returns Calendar.class.
     */
    public void testGetSupportedType_shouldReturnCalendarClass() {
        // Arrange: Get the singleton instance of the converter.
        CalendarConverter converter = CalendarConverter.INSTANCE;

        // Act: Call the method under test.
        Class<?> actualSupportedType = converter.getSupportedType();

        // Assert: Verify that the returned type is Calendar.class.
        assertEquals(Calendar.class, actualSupportedType);
    }
}