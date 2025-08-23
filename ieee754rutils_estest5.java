package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    @Test
    public void testMinShouldReturnSmallestValueFromArray() {
        // Arrange
        // An array containing a mix of positive, zero, and negative values.
        final double[] numbers = { 12.3, 0.0, -567.84087, 45.6, -1.0 };
        final double expectedMinimum = -567.84087;

        // Act
        final double actualMinimum = IEEE754rUtils.min(numbers);

        // Assert
        // The delta is 0.0 because no floating-point arithmetic that could
        // introduce precision errors is performed on the expected value.
        assertEquals(expectedMinimum, actualMinimum, 0.0);
    }
}