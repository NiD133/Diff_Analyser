package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    @Test
    public void minShouldReturnFirstArgumentWhenItIsSmaller() {
        // Arrange: Define two double values where the first is smaller than the second.
        final double smallerValue = 0.0;
        final double largerValue = 1460.933541;

        // Act: Call the min method with the defined values.
        final double result = IEEE754rUtils.min(smallerValue, largerValue);

        // Assert: Verify that the result is the smaller of the two values.
        assertEquals(smallerValue, result, 0.0);
    }
}