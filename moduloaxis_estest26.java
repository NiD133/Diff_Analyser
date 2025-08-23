package org.jfree.chart.axis;

import org.jfree.data.Range;
import org.junit.Test;

/**
 * Unit tests for the {@link ModuloAxis} class.
 */
public class ModuloAxisTest {

    /**
     * Verifies that calling setDisplayRange() throws a NullPointerException
     * if the axis was constructed with a null fixedRange.
     */
    @Test(expected = NullPointerException.class)
    public void setDisplayRangeShouldThrowNullPointerExceptionWhenFixedRangeIsNull() {
        // Arrange: Create a ModuloAxis with a null fixed range. The constructor
        // allows this, but methods that rely on the range are expected to fail.
        ModuloAxis axis = new ModuloAxis("Test Axis", null);

        // Act: Attempt to set the display range. This action should trigger the
        // NullPointerException because the method requires the fixedRange to map values.
        axis.setDisplayRange(0.0, 100.0);

        // Assert: The expected exception is declared in the @Test annotation,
        // so no further assertion is needed. The test will pass if the
        // exception is thrown and fail otherwise.
    }
}