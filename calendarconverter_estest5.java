package org.joda.time.convert;

import org.joda.time.Chronology;
import org.junit.Test;

/**
 * Unit tests for {@link CalendarConverter}.
 * This test focuses on the handling of null inputs.
 */
public class CalendarConverterTest { // Renamed for clarity, removing EvoSuite-specific naming.

    /**
     * Tests that getInstantMillis() throws a NullPointerException when the input object is null.
     * The Joda-Time conversion framework mandates that converters throw a NullPointerException
     * for null input objects to ensure consistent and predictable behavior.
     */
    @Test(expected = NullPointerException.class)
    public void getInstantMillis_shouldThrowNullPointerException_whenObjectIsNull() {
        // Arrange
        CalendarConverter converter = CalendarConverter.INSTANCE;
        Chronology chronology = null; // The value of the chronology does not affect this specific test

        // Act & Assert
        // The following call is expected to throw a NullPointerException because the first argument is null.
        converter.getInstantMillis(null, chronology);
    }
}