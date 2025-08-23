package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link ModuloAxis} class, focusing on its equality contract
 * and constructor behavior.
 */
public class ModuloAxisTest {

    /**
     * Verifies that the constructor correctly initializes the default display range.
     * The default display should start at 270.0 and end at 90.0, which is
     * typical for a compass-like display where 0/360 is at the top.
     */
    @Test
    public void constructorShouldSetDefaultDisplayStartAndEnd() {
        // Arrange: Create a ModuloAxis with a standard range.
        Range fixedRange = new Range(0.0, 360.0);
        ModuloAxis axis = new ModuloAxis("Angle", fixedRange);

        // Act: The constructor has already acted, so we just get the values.
        double displayStart = axis.getDisplayStart();
        double displayEnd = axis.getDisplayEnd();

        // Assert: Check that the display range is set to its default values.
        assertEquals("Default display start should be 270.0", 270.0, displayStart, 0.0);
        assertEquals("Default display end should be 90.0", 90.0, displayEnd, 0.0);
    }

    /**
     * Verifies that the equals() method returns false when a ModuloAxis
     * is compared with an object of a different, incompatible type.
     */
    @Test
    public void equalsShouldReturnFalseForIncompatibleObjectType() {
        // Arrange: Create a ModuloAxis instance and an object of a different type.
        Range fixedRange = new Range(0.0, 360.0);
        ModuloAxis axis = new ModuloAxis("Test Axis", fixedRange);
        Object incompatibleObject = TickType.MINOR;

        // Act: Compare the axis with the incompatible object.
        boolean isEqual = axis.equals(incompatibleObject);

        // Assert: The result should be false.
        assertFalse("equals() should return false when comparing with a different object type.", isEqual);
    }
}