package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

/**
 * Tests for the {@link ModuloAxis} class.
 */
public class ModuloAxisTest {

    /**
     * Verifies that manually resizing the axis range disables the auto-range feature.
     * A NumberAxis (the superclass) is initialized with auto-range enabled by default.
     * Any manual call to resizeRange() should disable this behavior.
     */
    @Test
    public void resizeRangeShouldDisableAutoRange() {
        // Arrange: Create a ModuloAxis, which should have auto-range enabled by default.
        Range fixedRange = new Range(0.0, 360.0);
        ModuloAxis moduloAxis = new ModuloAxis("Angle", fixedRange);

        // Pre-condition check: Ensure auto-range is initially true.
        assertTrue("A new axis should have auto-range enabled by default.",
                moduloAxis.isAutoRange());

        // Act: Manually resize the axis range. The specific parameters are not
        // critical; any call to resizeRange should trigger the behavior.
        moduloAxis.resizeRange(0.5, 180.0);

        // Assert: Verify that auto-range is now disabled.
        assertFalse("Calling resizeRange() should disable the auto-range feature.",
                moduloAxis.isAutoRange());
    }
}