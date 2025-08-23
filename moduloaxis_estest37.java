package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * This test class was improved from an auto-generated test. In a real-world
 * scenario, the class name 'ModuloAxis_ESTestTest37' would be renamed to
 * 'ModuloAxisTest'.
 */
public class ModuloAxis_ESTestTest37 {

    /**
     * Verifies that a newly created ModuloAxis instance has the correct default
     * display range and that its equals() method is reflexive (an object is
     * equal to itself).
     */
    @Test
    public void newInstance_shouldHaveDefaultDisplayRangeAndBeEqualToItself() {
        // Arrange: Create a ModuloAxis with a sample range and label.
        Range fixedRange = new Range(0.0, 360.0);
        ModuloAxis axis = new ModuloAxis("Test Axis", fixedRange);

        // Assert: Check the initial state and basic object contract.

        // 1. Verify that the constructor sets the default display range correctly.
        // The default is a circle-like range from 270 to 90 degrees.
        assertEquals("Default display start value should be 270.0", 270.0, axis.getDisplayStart(), 0.01);
        assertEquals("Default display end value should be 90.0", 90.0, axis.getDisplayEnd(), 0.01);

        // 2. Verify the reflexivity property of the equals() method.
        assertTrue("An object should always be equal to itself.", axis.equals(axis));
    }
}