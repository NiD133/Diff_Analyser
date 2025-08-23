package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * A test suite for the {@link MeterNeedle} class, focusing on the equals() method.
 */
public class MeterNeedle_ESTestTest15 {

    /**
     * Verifies that the equals() method returns false when comparing instances
     * of two different MeterNeedle subclasses.
     */
    @Test
    public void equals_whenComparingDifferentNeedleTypes_shouldReturnFalse() {
        // Arrange: Create two different types of MeterNeedle.
        // WindNeedle and PlumNeedle are concrete subclasses of MeterNeedle.
        // By default, they share the same initial property values.
        MeterNeedle windNeedle = new WindNeedle();
        MeterNeedle plumNeedle = new PlumNeedle();

        // Act: Compare the two different needle objects for equality.
        boolean areEqual = windNeedle.equals(plumNeedle);

        // Assert: The result should be false because the objects are of
        // different classes, even if their internal states are identical.
        assertFalse("Instances of different MeterNeedle subclasses should not be equal.", areEqual);
    }
}