package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Unit tests for the {@link SegmentUtils} class.
 */
public class SegmentUtilsTest {

    /**
     * Tests that {@link SegmentUtils#countArgs(String)} correctly returns 0
     * for a method descriptor that has no arguments.
     */
    @Test
    public void countArgsShouldReturnZeroForMethodWithNoArguments() {
        // Arrange: A standard Java method descriptor for a method that takes no arguments
        // and returns void. The key part is the empty parentheses "()".
        final String descriptorWithNoArgs = "()V";

        // Act: Call the method to count the arguments in the descriptor.
        final int argumentCount = SegmentUtils.countArgs(descriptorWithNoArgs);

        // Assert: The result should be 0, as there are no types listed between the parentheses.
        assertEquals(0, argumentCount);
    }
}