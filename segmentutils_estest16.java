package org.apache.commons.compress.harmony.unpack200;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Unit tests for the utility methods in {@link SegmentUtils}.
 */
public class SegmentUtilsTest {

    /**
     * Tests that {@code countArgs} throws an {@code IllegalArgumentException} when processing a
     * method descriptor that is malformed (specifically, missing its closing parenthesis).
     */
    @Test
    public void countArgsShouldThrowExceptionForDescriptorMissingClosingParenthesis() {
        // A malformed method descriptor string that contains an opening parenthesis
        // but lacks the corresponding closing parenthesis.
        final String malformedDescriptor = "xYt>#a(6={";

        // The value for the width of longs and doubles is irrelevant for this test case,
        // as the input string's structural parsing is expected to fail before this value is used.
        final int widthOfLongsAndDoubles = 2;

        try {
            SegmentUtils.countArgs(malformedDescriptor, widthOfLongsAndDoubles);
            fail("Expected an IllegalArgumentException to be thrown for a malformed descriptor.");
        } catch (final IllegalArgumentException e) {
            // The implementation is expected to throw an exception with this specific
            // message when the closing parenthesis is not found.
            assertEquals("No arguments", e.getMessage());
        }
    }
}