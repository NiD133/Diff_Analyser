package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * This test suite contains improved, human-readable tests for the {@link SegmentUtils} class.
 * The original tests were auto-generated and have been refactored for better clarity and maintainability.
 */
public class SegmentUtilsTest {

    /**
     * Tests that {@code countInvokeInterfaceArgs} correctly returns 0 for a method descriptor
     * that has no arguments.
     *
     * The test uses a string where the empty argument list "()" is surrounded by other
     * characters to ensure the parsing logic correctly isolates and processes only the
     * argument descriptor part.
     */
    @Test
    public void countInvokeInterfaceArgsShouldReturnZeroForDescriptorWithNoArguments() {
        // Arrange: Define the input and the expected outcome.
        // The key part of this descriptor is "()", indicating zero arguments.
        final String descriptorWithNoArgs = "1.8()JbKnQPeYyNxq!";
        final int expectedArgumentCount = 0;

        // Act: Call the method under test.
        final int actualArgumentCount = SegmentUtils.countInvokeInterfaceArgs(descriptorWithNoArgs);

        // Assert: Verify that the actual result matches the expected result.
        assertEquals("The method descriptor '()' should result in an argument count of 0.",
                     expectedArgumentCount,
                     actualArgumentCount);
    }
}