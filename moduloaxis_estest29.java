package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;

/**
 * Unit tests for the {@link ModuloAxis} class, focusing on its behavior
 * under specific edge cases.
 */
public class ModuloAxisTest {

    /**
     * Verifies that calling resizeRange() on an axis initialized with a null
     * 'fixedRange' throws a NullPointerException. The resize operation
     * depends on the fixedRange for its calculations and cannot proceed without it.
     */
    @Test(expected = NullPointerException.class)
    public void resizeRange_whenFixedRangeIsNull_shouldThrowNullPointerException() {
        // Arrange: Create a ModuloAxis instance with a null fixedRange.
        // The constructor accepts a null range, but methods that rely on it should fail.
        ModuloAxis axis = new ModuloAxis("Test Axis", null);

        // Act: Attempt to resize the range. This action is expected to trigger the exception.
        axis.resizeRange(0.50); // The percentage value is arbitrary.

        // Assert: The test passes if a NullPointerException is thrown,
        // which is handled by the @Test(expected=...) annotation.
    }
}