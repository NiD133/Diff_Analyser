package org.jfree.chart.axis;

import org.junit.Test;
import java.util.TimeZone;
import static org.junit.Assert.assertFalse;

/**
 * A test case for the equals() method of the {@link QuarterDateFormat} class.
 */
public class QuarterDateFormat_ESTestTest11 {

    /**
     * Verifies that the equals() method returns false when comparing two
     * instances of QuarterDateFormat that were constructed with different
     * properties (specifically, different quarter symbols and a different
     * 'quarterFirst' flag).
     */
    @Test
    public void equals_shouldReturnFalse_whenInstancesHaveDifferentProperties() {
        // Arrange
        TimeZone commonTimeZone = TimeZone.getDefault();

        // Create an instance with default settings: regular quarter symbols ("1", "2", ..)
        // and year-first formatting (e.g., "2023 1").
        QuarterDateFormat defaultFormat = new QuarterDateFormat(commonTimeZone);

        // Create another instance with custom settings: Roman numeral symbols ("I", "II", ..)
        // and quarter-first formatting (e.g., "I 2023").
        QuarterDateFormat customFormat = new QuarterDateFormat(
                commonTimeZone, QuarterDateFormat.ROMAN_QUARTERS, true);

        // Act & Assert
        // The two instances should not be equal because both their quarter symbols
        // and their 'quarterFirst' formatting flag are different.
        assertFalse(defaultFormat.equals(customFormat));
    }
}