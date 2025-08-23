package org.joda.time.convert;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for {@link CalendarConverter}.
 */
public class CalendarConverterTest {

    /**
     * Tests that the toString() method returns a standard representation,
     * including the name of the type it converts.
     */
    @Test
    public void toString_shouldReturnCorrectRepresentation() {
        // The expected format is "Converter[<supported-class-name>]"
        String expected = "Converter[java.util.Calendar]";
        
        assertEquals(expected, CalendarConverter.INSTANCE.toString());
    }
}