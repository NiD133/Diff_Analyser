package org.jfree.chart.axis;

import org.jfree.data.time.DateRange;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link ModuloAxis} class.
 */
// The original test class name and inheritance are kept to maintain compatibility
// with the existing test suite structure.
public class ModuloAxis_ESTestTest47 extends ModuloAxis_ESTest_scaffolding {

    /**
     * Verifies that the ModuloAxis constructor correctly initializes the default display range.
     * The default display range is expected to be from 270.0 to 90.0, which is a
     * common configuration for representing compass headings or angles.
     */
    @Test
    public void constructorShouldSetDefaultDisplayRange() {
        // Arrange: Define the required parameters for the ModuloAxis constructor.
        // The specific range doesn't affect this test, only that it's a valid Range object.
        DateRange fixedRange = DateAxis.DEFAULT_DATE_RANGE;
        String axisLabel = "Test Modulo Axis";

        // Act: Create a new instance of ModuloAxis.
        ModuloAxis axis = new ModuloAxis(axisLabel, fixedRange);

        // Assert: Check that the display start and end values are set to their expected defaults.
        assertEquals("The default display start value should be 270.0.",
                270.0, axis.getDisplayStart(), 0.01);
        assertEquals("The default display end value should be 90.0.",
                90.0, axis.getDisplayEnd(), 0.01);
    }
}