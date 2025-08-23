package org.jfree.chart.plot;

import org.jfree.data.Range;
import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Unit tests for the equals() method of the {@link MeterInterval} class.
 */
public class MeterIntervalEqualsTest {

    /**
     * Verifies that two MeterInterval instances are not equal if they share the
     * same data range but have different labels. The label is a key component
     * of the interval's identity.
     */
    @Test
    public void equals_shouldReturnFalse_whenIntervalsHaveDifferentLabels() {
        // Arrange: Create two MeterInterval objects with the same range but different labels.
        Range commonRange = new Range(0.0, 50.0);
        MeterInterval intervalNormal = new MeterInterval("Normal", commonRange);
        MeterInterval intervalHigh = new MeterInterval("High", commonRange);

        // Act & Assert: The two intervals should not be considered equal.
        assertFalse("Intervals with different labels should not be equal.", intervalNormal.equals(intervalHigh));
    }
}