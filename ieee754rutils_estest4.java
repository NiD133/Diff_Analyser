package org.apache.commons.lang3.math;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Test suite for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    @Test
    public void minOfSingleElementArrayShouldReturnTheElement() {
        // Arrange: Create an array with a single element.
        final double[] values = {1.0};
        final double expected = 1.0;

        // Act: Call the min method with the single-element array.
        final double actual = IEEE754rUtils.min(values);

        // Assert: The result should be the single element in the array.
        assertEquals("The minimum of a single-element array should be the element itself.", expected, actual, 0.0);
    }
}