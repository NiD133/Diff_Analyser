package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Verifies that the constructor throws an IllegalArgumentException when the
     * 'range' argument is null. The constructor has a non-null check for this
     * critical parameter.
     */
    @Test
    public void constructor_withNullRange_throwsIllegalArgumentException() {
        // Act and Assert
        try {
            // Attempt to create an interval with a valid label but a null range.
            new MeterInterval("Normal", null);
            fail("Expected an IllegalArgumentException, but it was not thrown.");
        } catch (IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("Null 'range' argument.", e.getMessage());
        }
    }
}