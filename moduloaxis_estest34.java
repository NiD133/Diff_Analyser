package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link ModuloAxis} class, focusing on its equality logic and constructor behavior.
 */
public class ModuloAxisTest {

    private static final double DELTA = 0.01;

    /**
     * Verifies that the constructor correctly initializes the axis with its default
     * display start and end values.
     */
    @Test
    public void constructor_shouldSetDefaultDisplayRange() {
        // Arrange
        String axisLabel = "Angle";
        Range fixedRange = new Range(0.0, 360.0);
        
        // Act
        ModuloAxis axis = new ModuloAxis(axisLabel, fixedRange);

        // Assert
        // The default display range is from 270 to 90 degrees, which is useful
        // for representations like a compass where 0/360 is at the top.
        assertEquals("Default display start value should be 270.0", 270.0, axis.getDisplayStart(), DELTA);
        assertEquals("Default display end value should be 90.0", 90.0, axis.getDisplayEnd(), DELTA);
    }

    /**
     * Tests the equals() method, ensuring that two ModuloAxis instances with
     * different 'fixedRange' properties are not considered equal.
     */
    @Test
    public void equals_shouldReturnFalse_whenFixedRangesDiffer() {
        // Arrange
        String axisLabel = "Test Axis";
        ModuloAxis axisWithNullRange = new ModuloAxis(axisLabel, null);
        
        // The DEFAULT_RANGE is inherited from ValueAxis and is new Range(0.0, 1.0).
        ModuloAxis axisWithDefaultRange = new ModuloAxis(axisLabel, ModuloAxis.DEFAULT_RANGE);

        // Act & Assert
        // The 'fixedRange' is a critical part of the axis's state, so a difference
        // should result in inequality.
        assertNotEquals("Axes with different fixed ranges should not be equal",
                axisWithDefaultRange, axisWithNullRange);
    }

    /**
     * Tests the equals() method, ensuring that two ModuloAxis instances with
     * identical properties are considered equal.
     */
    @Test
    public void equals_shouldReturnTrue_whenPropertiesAreIdentical() {
        // Arrange
        String axisLabel = "Test Axis";
        Range range = new Range(0.0, 100.0);
        ModuloAxis axis1 = new ModuloAxis(axisLabel, range);
        ModuloAxis axis2 = new ModuloAxis(axisLabel, range);

        // Act & Assert
        assertEquals("Axes with identical properties should be equal", axis1, axis2);
        assertEquals("Hash codes for equal objects must be the same", axis1.hashCode(), axis2.hashCode());
    }
}