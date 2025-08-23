package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link ModuloAxis} class.
 */
public class ModuloAxisTest {

    private static final double DELTA = 1e-9;

    /**
     * Verifies that getDisplayStart() returns the correct value after
     * setDisplayRange() has been called.
     */
    @Test
    public void setDisplayRangeShouldCorrectlySetDisplayStart() {
        // Arrange: Create a ModuloAxis with a fixed range of 0 to 100.
        // This simplified setup is equivalent to the original test's use of a
        // ThermometerPlot, which defaults to the same range.
        Range fixedRange = new Range(0.0, 100.0);
        ModuloAxis moduloAxis = new ModuloAxis("Test Axis", fixedRange);

        double expectedStart = 0.0;
        double displayEnd = 10.0;

        // Act: Set a new display range on the axis.
        moduloAxis.setDisplayRange(expectedStart, displayEnd);
        double actualStart = moduloAxis.getDisplayStart();

        // Assert: The retrieved display start value should match the one that was set.
        assertEquals("The display start value should match the value set via setDisplayRange.",
                expectedStart, actualStart, DELTA);
    }
}