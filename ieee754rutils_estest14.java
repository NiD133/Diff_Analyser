package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that {@link IEEE754rUtils#max(float...)} correctly finds the maximum value
     * in an array containing only negative floats. The maximum value in this case
     * is the one closest to zero.
     */
    @Test
    public void max_shouldReturnLargestValue_whenArrayContainsOnlyNegativeFloats() {
        // Arrange
        final float[] inputNumbers = {-835.94F, -1742.84F, -1.0F};
        final float expectedMax = -1.0F;

        // Act
        final float actualMax = IEEE754rUtils.max(inputNumbers);

        // Assert
        // The delta is set to 0.0f as we expect an exact match for this input.
        assertEquals(expectedMax, actualMax, 0.0f);
    }
}