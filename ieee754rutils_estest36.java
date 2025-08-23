package org.apache.commons.lang3.math;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Test suite for the {@link IEEE754rUtils} class.
 */
public class IEEE754rUtilsTest {

    @Test
    public void minOfEmptyDoubleArrayShouldThrowIllegalArgumentException() {
        // Arrange: Create an empty array to test the edge case.
        final double[] emptyArray = new double[0];

        // Act & Assert: Verify that the method throws the correct exception with the expected message.
        try {
            IEEE754rUtils.min(emptyArray);
            fail("Expected an IllegalArgumentException to be thrown for an empty array.");
        } catch (final IllegalArgumentException e) {
            // The Javadoc states that an empty array is illegal.
            // This also verifies that the underlying validation provides a clear message.
            assertEquals("The validated array is empty", e.getMessage());
        }
    }
}