package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    /**
     * Tests that {@link IEEE754rUtils#min(double...)} correctly returns 0.0
     * when the input array contains only zero values.
     */
    @Test
    public void minShouldReturnZeroForArrayOfZeros() {
        // Arrange: Create an array containing only the value 0.0.
        // While `new double[4]` would produce the same result, being explicit
        // about the array's contents makes the test's intent clearer.
        final double[] numbers = {0.0, 0.0, 0.0, 0.0};
        final double expectedMinimum = 0.0;

        // Act: Call the method under test.
        final double actualMinimum = IEEE754rUtils.min(numbers);

        // Assert: Verify that the result is 0.0.
        // A delta of 0.0 is used for an exact comparison.
        assertEquals("The minimum of an array of zeros should be zero", expectedMinimum, actualMinimum, 0.0);
    }
}