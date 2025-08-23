package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Unit tests for the {@link ModuloAxis} class.
 */
public class ModuloAxisTest {

    /**
     * Verifies that calling resizeRange() with a zero percent value throws an
     * exception if the axis was constructed with a null fixed range.
     * <p>
     * According to the NumberAxis implementation, a resize operation with zero
     * percent triggers an auto-range adjustment. For ModuloAxis, this
     * adjustment attempts to use its configured fixed range. This test ensures
     * that if the fixed range is null, the operation fails with a clear error,
     * as expected.
     */
    @Test
    public void resizeRangeWithZeroPercentShouldThrowExceptionWhenFixedRangeIsNull() {
        // Arrange: Create a ModuloAxis with a null fixed range.
        ModuloAxis axis = new ModuloAxis("Test Axis", null);

        // Act & Assert: Expect an IllegalArgumentException when resizing with 0%.
        try {
            axis.resizeRange(0.0, 0.0);
            fail("Expected an IllegalArgumentException to be thrown, but no exception occurred.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception message is correct.
            assertEquals("Null 'range' argument.", e.getMessage());
        }
    }
}