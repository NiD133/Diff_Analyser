package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.fail;

/**
 * This class contains improved, more understandable tests for the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Tests that the {@code countArgs} method throws an {@link IllegalArgumentException}
     * when the {@code widthOfLongsAndDoubles} parameter is negative.
     * A valid method descriptor is used to ensure the test fails specifically
     * due to the negative width, isolating the condition under test.
     */
    @Test
    public void countArgs_withNegativeWidth_shouldThrowIllegalArgumentException() {
        // Arrange: Define the inputs for the test case.
        // A valid-but-simple method descriptor ensures that the exception is not
        // caused by an invalid descriptor format.
        final String validDescriptor = "()V";
        final int negativeWidth = -1;

        // Act & Assert: Call the method and verify the expected exception is thrown.
        try {
            SegmentUtils.countArgs(validDescriptor, negativeWidth);
            fail("Expected an IllegalArgumentException for negative width, but none was thrown.");
        } catch (IllegalArgumentException expected) {
            // The expected exception was caught, so the test passes.
            // The original test did not verify the exception message, so we maintain that behavior.
        }
    }
}