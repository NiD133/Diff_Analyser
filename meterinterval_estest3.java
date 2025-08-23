package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link MeterInterval} class, focusing on the getRange() method.
 */
public class MeterIntervalTest {

    /**
     * Verifies that getRange() returns the same Range object
     * that was provided in the constructor.
     */
    @Test
    public void getRange_ShouldReturnRangeProvidedInConstructor() {
        // Arrange: Create a MeterInterval with a specific, descriptive range.
        Range expectedRange = new Range(25.0, 75.0);
        MeterInterval meterInterval = new MeterInterval("Normal", expectedRange);

        // Act: Retrieve the range from the MeterInterval instance.
        Range actualRange = meterInterval.getRange();

        // Assert: The retrieved range should be identical to the one set initially.
        assertEquals(expectedRange, actualRange);
    }
}