package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link MeterNeedle} class, focusing on its equality contract.
 */
public class MeterNeedleTest {

    /**
     * Verifies that the equals() method returns false when comparing two instances
     * of different MeterNeedle subclasses.
     */
    @Test
    public void equals_comparingDifferentSubclassInstances_returnsFalse() {
        // Arrange: Create two different types of MeterNeedle.
        // According to the equals() contract, objects of different classes
        // should not be considered equal.
        MeterNeedle middlePinNeedle = new MiddlePinNeedle();
        MeterNeedle longNeedle = new LongNeedle();

        // Act: Compare the two objects for equality.
        boolean areEqual = middlePinNeedle.equals(longNeedle);

        // Assert: The result should be false.
        assertFalse("Instances of different MeterNeedle subclasses should not be equal.", areEqual);
    }
}