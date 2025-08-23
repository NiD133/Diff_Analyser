package org.jfree.chart.axis;

import org.junit.Test;
import java.util.TimeZone;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the equals() method in the {@link QuarterDateFormat} class.
 */
public class QuarterDateFormatTest {

    /**
     * Verifies that two QuarterDateFormat instances created with the same
     * TimeZone and default settings are considered equal.
     */
    @Test
    public void equals_shouldReturnTrue_forInstancesWithIdenticalConfiguration() {
        // Arrange: Create two QuarterDateFormat instances with the same time zone.
        // The other properties (quarter symbols, quarterFirst flag) will use default values.
        TimeZone timeZone = TimeZone.getDefault();
        QuarterDateFormat formatter1 = new QuarterDateFormat(timeZone);
        QuarterDateFormat formatter2 = new QuarterDateFormat(timeZone);

        // Act & Assert: The two instances should be equal.
        // We use assertEquals for a more expressive test and a clearer failure message
        // compared to assertTrue(formatter1.equals(formatter2)).
        assertEquals(formatter1, formatter2);
    }
}