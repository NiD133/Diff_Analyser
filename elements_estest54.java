package org.jsoup.select;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for exception handling in the {@link Elements} class.
 */
public class ElementsExceptionTest {

    /**
     * Verifies that calling the {@code wrap()} method with a null string
     * throws an {@link IllegalArgumentException}. This ensures that the
     * input validation for the wrapping HTML is working correctly.
     */
    @Test
    public void wrapWithNullStringShouldThrowIllegalArgumentException() {
        // Arrange: Create an empty Elements collection.
        Elements elements = new Elements();

        // Act & Assert: Attempt the invalid operation and verify the exception.
        try {
            elements.wrap(null);
            fail("Expected an IllegalArgumentException to be thrown for a null wrap string.");
        } catch (IllegalArgumentException e) {
            // Verify that the correct exception was thrown with the expected message.
            assertEquals("String must not be empty", e.getMessage());
        }
    }
}