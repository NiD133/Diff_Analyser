package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.util.Calendar;

/**
 * Unit tests for {@link CalendarConverter}.
 */
public class CalendarConverterTest {

    /**
     * Tests that getSupportedType() correctly returns Calendar.class.
     */
    @Test
    public void getSupportedType_shouldReturnCalendarClass() {
        // Arrange: Get the singleton instance of the converter.
        CalendarConverter converter = CalendarConverter.INSTANCE;
        
        // Act: Call the method under test.
        Class<?> actualSupportedType = converter.getSupportedType();
        
        // Assert: Verify that the returned type is exactly Calendar.class.
        assertEquals(Calendar.class, actualSupportedType);
    }
}