package org.threeten.extra;

import static org.junit.Assert.assertTrue;

import java.time.temporal.ChronoField;
import java.time.temporal.TemporalField;
import org.junit.Test;

/**
 * This test suite focuses on the {@link DayOfMonth#isSupported(TemporalField)} method.
 */
public class DayOfMonthTest {

    /**
     * Tests that the {@link DayOfMonth#isSupported(TemporalField)} method correctly
     * identifies {@link ChronoField#DAY_OF_MONTH} as a supported field.
     */
    @Test
    public void isSupported_returnsTrue_forDayOfMonthField() {
        // Arrange: Create a DayOfMonth instance and specify the field to check.
        DayOfMonth dayOfMonth = DayOfMonth.of(15);
        TemporalField fieldToTest = ChronoField.DAY_OF_MONTH;

        // Act: Call the isSupported method.
        boolean isSupported = dayOfMonth.isSupported(fieldToTest);

        // Assert: Verify that the field is reported as supported.
        assertTrue("The DAY_OF_MONTH field should always be supported.", isSupported);
    }
}