package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import org.junit.Test;

/**
 * Unit tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that IEEE754rUtils.min() returns 0.0F for an array containing only zeros.
     */
    @Test
    public void minFloatArray_shouldReturnZero_whenArrayContainsOnlyZeros() {
        // Arrange: Create an array containing two zero values.
        final float[] values = {0.0F, 0.0F};
        final float expectedMinimum = 0.0F;

        // Act: Call the method under test.
        final float actualMinimum = IEEE754rUtils.min(values);

        // Assert: Verify that the result is the expected minimum value.
        // A delta of 0.0F is used for an exact comparison.
        assertEquals(expectedMinimum, actualMinimum, 0.0F);
    }
}