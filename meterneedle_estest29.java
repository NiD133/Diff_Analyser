package org.jfree.chart.plot.compass;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

/**
 * Tests for the {@link MeterNeedle#equals(Object)} method.
 * This test focuses on comparing different concrete implementations of MeterNeedle.
 */
public class MeterNeedleEqualsTest {

    /**
     * Verifies that the equals() method returns false when comparing two MeterNeedle
     * objects of different concrete types (e.g., ShipNeedle and PointerNeedle).
     */
    @Test
    public void equals_shouldReturnFalse_whenComparingDifferentNeedleTypes() {
        // Arrange: Create two MeterNeedle instances of different concrete types.
        // Both are initialized with their default values.
        MeterNeedle shipNeedle = new ShipNeedle();
        MeterNeedle pointerNeedle = new PointerNeedle();

        // Act: Compare the two objects using the equals() method.
        boolean areEqual = shipNeedle.equals(pointerNeedle);

        // Assert: Verify that the two needles are not considered equal, as they
        // are instances of different classes.
        assertFalse("MeterNeedle instances of different types should not be equal.", areEqual);
    }
}