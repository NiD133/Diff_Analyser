package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link MeterInterval} class.
 */
public class MeterIntervalTest {

    /**
     * Verifies that the getRange() method correctly returns the Range object
     * that was provided in the constructor. This test specifically checks the handling
     * of a Range with NaN (Not a Number) bounds.
     */
    @Test
    public void getRangeShouldReturnTheRangeProvidedInTheConstructor() {
        // Arrange: Create a MeterInterval with a specific Range.
        // The original test used a convoluted method to create a Range(NaN, NaN).
        // We create it directly for clarity and simplicity.
        String label = "Test Interval";
        Range expectedRange = new Range(Double.NaN, Double.NaN);
        MeterInterval meterInterval = new MeterInterval(label, expectedRange);

        // Act: Retrieve the range from the MeterInterval.
        Range actualRange = meterInterval.getRange();

        // Assert: The retrieved range should be identical to the one provided.
        // We check both bounds for completeness and include messages for clarity on failure.
        assertEquals("The lower bound of the range should match",
                expectedRange.getLowerBound(), actualRange.getLowerBound(), 0.0);
        assertEquals("The upper bound of the range should match",
                expectedRange.getUpperBound(), actualRange.getUpperBound(), 0.0);
    }
}