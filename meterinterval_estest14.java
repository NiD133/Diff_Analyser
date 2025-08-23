package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the equals() method in the {@link MeterInterval} class.
 */
public class MeterIntervalEqualsTest {

    /**
     * Verifies that two MeterInterval instances are not considered equal if their
     * underlying Range contains NaN values. This is the expected behavior because
     * Double.NaN is never equal to itself, a property that propagates up through
     * the Range.equals() method.
     */
    @Test
    public void equals_shouldReturnFalse_whenRangeContainsNaN() {
        // Arrange: Create two MeterIntervals that are identical except for their
        // reference, both using a Range defined with NaN.
        Range nanRange = new Range(Double.NaN, Double.NaN);
        MeterInterval interval1 = new MeterInterval("Normal", nanRange);
        MeterInterval interval2 = new MeterInterval("Normal", nanRange);

        // Act: Compare the two intervals for equality.
        boolean areEqual = interval1.equals(interval2);

        // Assert: The intervals should not be equal due to the NaN property.
        assertFalse("Two intervals with NaN-based ranges should not be equal.", areEqual);
    }
}