package org.apache.commons.lang3;

import org.junit.Test;
import static org.junit.Assert.assertNull;

/**
 * This test class contains improved tests for the {@link CharSetUtils} class.
 * The original test was auto-generated and has been rewritten for clarity.
 */
public class CharSetUtils_ESTestTest13 {

    /**
     * Tests that CharSetUtils.keep() returns null when the input string is null,
     * regardless of the character set provided. This behavior is documented in the
     * method's Javadoc.
     */
    @Test
    public void testKeepWithNullStringShouldReturnNull() {
        // Arrange: Define a character set. Its content is irrelevant for this test
        // because the method should return null for any null input string.
        final String[] characterSet = {"a", "b", "c"};

        // Act: Call the keep method with a null input string.
        final String result = CharSetUtils.keep(null, characterSet);

        // Assert: Verify that the result is null.
        assertNull("keep(null, ...) should return null.", result);
    }
}