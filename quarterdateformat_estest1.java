package org.jfree.chart.axis;

import org.junit.Test;
import java.util.TimeZone;
import static org.junit.Assert.assertNotEquals;

/**
 * Unit tests for the {@link QuarterDateFormat} class, focusing on its equality logic.
 */
public class QuarterDateFormatTest {

    /**
     * Verifies that the equals() method returns false when comparing two
     * QuarterDateFormat instances that have different quarter symbols and a
     * different 'quarterFirst' flag.
     */
    @Test
    public void equals_whenQuarterSymbolsAndOrderDiffer_shouldReturnFalse() {
        // Arrange: Create two formatters with different configurations.
        TimeZone timeZone = TimeZone.getDefault();

        // Instance 1: Uses default settings (regular symbols, year first).
        QuarterDateFormat defaultFormat = new QuarterDateFormat(timeZone);

        // Instance 2: Uses custom settings (Roman symbols, quarter first).
        QuarterDateFormat customFormat = new QuarterDateFormat(
                timeZone, QuarterDateFormat.ROMAN_QUARTERS, true);

        // Act & Assert: The two instances should not be considered equal.
        assertNotEquals(defaultFormat, customFormat);
    }
}