package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for the {@link ModuloAxis} class.
 */
public class ModuloAxisTest {

    /**
     * Verifies that the constructor correctly sets the default display start and end values.
     * The ModuloAxis is initialized with a display range of 270.0 to 90.0 by default.
     */
    @Test
    public void testDefaultDisplayRangeAfterConstruction() {
        // Arrange: Create a ModuloAxis instance. The specific range and label
        // do not affect the default display values being tested here.
        String axisLabel = "Angle (Degrees)";
        Range fixedRange = new Range(0.0, 360.0);
        ModuloAxis axis = new ModuloAxis(axisLabel, fixedRange);

        // Act: Retrieve the default display start and end values.
        double actualDisplayStart = axis.getDisplayStart();
        double actualDisplayEnd = axis.getDisplayEnd();

        // Assert: Check that the values match the expected defaults.
        assertEquals("Default display start value should be 270.0", 270.0, actualDisplayStart, 0.0);
        assertEquals("Default display end value should be 90.0", 90.0, actualDisplayEnd, 0.0);
    }
}