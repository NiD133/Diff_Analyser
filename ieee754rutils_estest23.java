package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * A test suite for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    @Test
    public void testMaxDoubleReturnsGreaterOfTwoValues() {
        // Arrange: Define the input values for the test.
        final double smallerValue = -4398.39854599338;
        final double largerValue = 564.128287262;

        // Act: Call the method under test.
        final double result = IEEE754rUtils.max(smallerValue, largerValue);

        // Assert: Verify the result is the expected larger value.
        assertEquals(largerValue, result, 0.0);
    }
}