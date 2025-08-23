package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test suite verifies the behavior of the {@link SegmentUtils} class,
 * particularly its handling of invalid inputs.
 */
public class SegmentUtilsTest {

    /**
     * Tests that {@code countArgs} throws an {@code IllegalArgumentException}
     * when the provided method descriptor string is invalid because it does not
     * start with an opening parenthesis '('.
     */
    @Test
    public void countArgsShouldThrowExceptionForDescriptorWithoutOpeningParenthesis() {
        // A valid Java method descriptor for counting arguments must start with '('.
        // This input is intentionally invalid to trigger the expected exception.
        final String invalidDescriptor = "tp!Hg y";
        final String expectedErrorMessage = "No arguments";

        try {
            SegmentUtils.countArgs(invalidDescriptor);
            fail("Expected an IllegalArgumentException because the descriptor is invalid.");
        } catch (final IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}