package org.apache.commons.compress.harmony.unpack200;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * This test suite contains tests for the {@link SegmentUtils} class.
 * This specific test case focuses on the behavior of the countInvokeInterfaceArgs method
 * when provided with invalid input.
 */
public class SegmentUtilsTest {

    /**
     * Tests that countInvokeInterfaceArgs throws an IllegalArgumentException
     * when given a method descriptor that does not start with a parenthesis '('.
     * A valid descriptor must begin with '(' to define its arguments.
     */
    @Test
    public void countInvokeInterfaceArgsShouldThrowExceptionForDescriptorWithoutArguments() {
        // A valid method descriptor must start with '(' to enclose the argument types.
        // This input string is invalid because it does not follow this format.
        final String invalidMethodDescriptor = "Aw2<'N6_{7~h_K?(gZ";
        final String expectedErrorMessage = "No arguments";

        try {
            SegmentUtils.countInvokeInterfaceArgs(invalidMethodDescriptor);
            fail("Expected an IllegalArgumentException to be thrown for an invalid descriptor.");
        } catch (IllegalArgumentException e) {
            // Verify that the exception has the expected message.
            assertEquals(expectedErrorMessage, e.getMessage());
        }
    }
}