package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;

/**
 * Tests for the equals() method in the {@link ModuloAxis} class.
 */
public class ModuloAxisEqualsTest {

    /**
     * This test verifies that calling equals() on a ModuloAxis instance
     * throws a NullPointerException if its 'fixedRange' field is null.
     * This situation occurs when the axis is constructed with a null range.
     * While this is likely a bug in the production code, this test
     * documents the current behavior.
     */
    @Test(expected = NullPointerException.class)
    public void equals_whenFixedRangeIsNull_shouldThrowNullPointerException() throws CloneNotSupportedException {
        // Arrange: Create a ModuloAxis with a null fixedRange.
        final String axisLabel = "Test Modulo Axis";
        final ModuloAxis axisWithNullRange = new ModuloAxis(axisLabel, null);
        
        // Create a clone to compare against. The bug occurs when the equals method
        // attempts to access a method on the null fixedRange field.
        final ModuloAxis clonedAxis = (ModuloAxis) axisWithNullRange.clone();

        // Act & Assert: Calling equals() is expected to throw a NullPointerException.
        // The assertion is handled declaratively by the 'expected' attribute of the @Test annotation.
        axisWithNullRange.equals(clonedAxis);
    }
}