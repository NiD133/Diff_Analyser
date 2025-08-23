package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class focuses on verifying the behavior of the ModuloAxis.
 * Note: The original test class name and inheritance from a scaffolding class
 * are preserved as they might be part of a larger, auto-generated test suite.
 * In a full manual refactoring, the class would typically be renamed to ModuloAxisTest.
 */
public class ModuloAxis_ESTestTest19 extends ModuloAxis_ESTest_scaffolding {

    /**
     * Verifies that calling autoAdjustRange() enables the auto-range flag
     * while preserving the default display start and end values set by the constructor.
     */
    @Test
    public void autoAdjustRangeShouldEnableAutoRangingAndRetainDefaultDisplayValues() {
        // Arrange: Create a ModuloAxis. The constructor sets default display values
        // (start=270.0, end=90.0) regardless of the fixed range.
        Range fixedRange = new Range(0.0, 360.0);
        ModuloAxis axis = new ModuloAxis("Test Modulo Axis", fixedRange);

        // Act: Call the method under test.
        axis.autoAdjustRange();

        // Assert: Check that auto-ranging is now enabled and that the display
        // range remains at its default initial values.
        assertTrue("The auto-range flag should be set to true after autoAdjustRange() is called.", axis.isAutoRange());
        assertEquals("The display start value should remain at its constructor-defined default.", 270.0, axis.getDisplayStart(), 0.01);
        assertEquals("The display end value should remain at its constructor-defined default.", 90.0, axis.getDisplayEnd(), 0.01);
    }
}