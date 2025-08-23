package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that {@link IEEE754rUtils#max(double, double, double)} returns the
     * correct value when all three arguments are identical.
     */
    @Test
    public void maxOfThreeDoubles_shouldReturnValue_whenAllInputsAreEqual() {
        // Arrange
        final double value = 1.0;
        final double expectedMax = 1.0;

        // Act
        final double actualMax = IEEE754rUtils.max(value, value, value);

        // Assert
        // A delta of 0.0 is used because no floating-point arithmetic is performed
        // that would introduce precision errors.
        assertEquals(expectedMax, actualMax, 0.0);
    }
}