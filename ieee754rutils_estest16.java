package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link IEEE754rUtils}.
 */
public class IEEE754rUtilsTest {

    @Test
    public void max_shouldReturnLargestValue_whenArrayContainsNegativeDoubles() {
        // Arrange: Define an array of negative numbers where -1.0 is the maximum value.
        final double[] numbers = {-1742.84, -163.0, -835.94, -1.0, -835.94, -1742.84, -1.0};
        final double expectedMax = -1.0;

        // Act: Call the max method with the array of negative numbers.
        final double actualMax = IEEE754rUtils.max(numbers);

        // Assert: The result should be the largest number in the array (the one closest to zero).
        assertEquals(expectedMax, actualMax, 0.0);
    }
}