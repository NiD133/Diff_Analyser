package org.joda.time.base;

import org.joda.time.DateTimeFieldType;
import org.joda.time.LocalDate;
import org.junit.Test;

import static org.junit.Assert.assertTrue;

/**
 * Unit test for the {@link AbstractPartial#isSupported(DateTimeFieldType)} method.
 *
 * <p>This test uses {@link LocalDate} as a concrete implementation of {@link AbstractPartial}
 * to verify its behavior.
 */
public class AbstractPartialIsSupportedTest {

    /**
     * Tests that isSupported() returns true for a field type that is part of the partial.
     */
    @Test
    public void isSupported_shouldReturnTrue_whenFieldTypeIsPresentInPartial() {
        // Arrange: Create a LocalDate instance, which is a partial containing year, month, and day.
        LocalDate partialDate = new LocalDate();
        DateTimeFieldType yearField = DateTimeFieldType.yearOfEra();

        // Act: Check if the 'yearOfEra' field is supported.
        boolean isYearSupported = partialDate.isSupported(yearField);

        // Assert: The 'yearOfEra' field should be supported by LocalDate.
        assertTrue("LocalDate should support the 'yearOfEra' field type.", isYearSupported);
    }
}