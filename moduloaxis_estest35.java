package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotEquals;

public class ModuloAxis_ESTestTest35 extends ModuloAxis_ESTest_scaffolding {

    /**
     * Verifies that two ModuloAxis instances are not equal after one of them
     * has its range resized. The equals() method should consider the axis range
     * in its comparison.
     */
    @Test
    public void equals_shouldReturnFalse_whenOneAxisHasRangeResized() {
        // Arrange: Create two identical ModuloAxis instances. A range of 0-360
        // is a common use case for a modulo axis (e.g., for degrees).
        String commonLabel = "Angle (Degrees)";
        Range initialRange = new Range(0.0, 360.0);
        ModuloAxis axis1 = new ModuloAxis(commonLabel, initialRange);
        ModuloAxis axis2 = new ModuloAxis(commonLabel, initialRange);

        // Sanity check: The two axes should be equal before any modifications.
        assertEquals("Initially, the two identical axes should be equal.", axis1, axis2);

        // Act: Resize the range of the second axis. This action should
        // differentiate it from the first axis.
        axis2.resizeRange(2.0); // Double the length of the axis range.

        // Assert: The axes should no longer be equal because their ranges differ.
        assertNotEquals("After resizing one axis, it should not be equal to the original.", axis1, axis2);

        // Also, verify the side-effect that resizing disables auto-ranging.
        assertFalse("Resizing the range should set auto-range to false.", axis2.isAutoRange());
    }
}