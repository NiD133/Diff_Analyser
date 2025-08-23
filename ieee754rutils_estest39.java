package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that IEEE754rUtils.max() correctly returns 0.0 when given an array
     * containing only zeros.
     */
    @Test
    public void maxShouldReturnZeroForArrayOfZeros() {
        // Arrange: Create an array containing only the value 0.0
        final double[] numbers = {0.0, 0.0, 0.0, 0.0};
        final double expectedMax = 0.0;

        // Act: Call the method under test
        final double actualMax = IEEE754rUtils.max(numbers);

        // Assert: Verify that the result is 0.0
        assertEquals("The maximum of an array of zeros should be 0.0", expectedMax, actualMax, 0.0);
    }
}