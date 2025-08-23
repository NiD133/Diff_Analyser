package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test suite contains improved, more understandable tests for the {@link SegmentUtils} class.
 * This specific test case focuses on how the countArgs method handles invalid input.
 */
public class SegmentUtilsTest {

    /**
     * Tests that countArgs() throws an IllegalArgumentException when the provided
     * method descriptor string is malformed (i.e., does not start with an opening parenthesis).
     */
    @Test
    public void countArgsThrowsExceptionForInvalidDescriptorFormat() {
        // A valid method descriptor must start with '('.
        // This input is invalid because it starts with a ')' instead.
        final String malformedDescriptor = ") 8FhA()w";
        final int arbitraryWidthForLongsAndDoubles = 10;

        try {
            SegmentUtils.countArgs(malformedDescriptor, arbitraryWidthForLongsAndDoubles);
            fail("Expected an IllegalArgumentException due to the malformed descriptor.");
        } catch (final IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("No arguments", e.getMessage());
        }
    }
}