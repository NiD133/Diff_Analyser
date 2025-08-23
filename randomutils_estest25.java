package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This class contains tests for the {@link RandomUtils} class.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class RandomUtils_ESTestTest25 extends RandomUtils_ESTest_scaffolding {

    /**
     * Tests that {@link RandomUtils#randomBytes(int)} throws an {@link IllegalArgumentException}
     * when the provided count is a negative number.
     */
    @Test(timeout = 4000)
    public void randomBytesShouldThrowIllegalArgumentExceptionForNegativeCount() {
        // Arrange: Set up the test conditions
        final RandomUtils randomUtils = new RandomUtils();
        final int negativeCount = -1;
        final String expectedErrorMessage = "Count cannot be negative.";

        // Act & Assert: Execute the test and verify the outcome
        try {
            randomUtils.randomBytes(negativeCount);
            fail("Expected an IllegalArgumentException to be thrown, but it wasn't.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}