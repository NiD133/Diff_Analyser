package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;

/**
 * Unit tests for the {@link ModuloAxis} class.
 */
public class ModuloAxisTest {

    /**
     * Verifies that calling resizeRange() with an anchor value throws a
     * NullPointerException if the axis was constructed with a null range.
     * This is expected because the resize calculation depends on the axis's
     * fixed range, which is null in this setup.
     */
    @Test(expected = NullPointerException.class)
    public void resizeRangeWithAnchorShouldThrowNPEWhenConstructedWithNullRange() {
        // Arrange: Create a ModuloAxis with a null fixedRange.
        ModuloAxis axis = new ModuloAxis("Test Axis", null);

        // Act: Attempt to resize the range. This should trigger an NPE.
        // The specific values for percent (2.0) and anchor (100.0) are arbitrary
        // as the exception occurs before they are used in the calculation.
        axis.resizeRange(2.0, 100.0);

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}