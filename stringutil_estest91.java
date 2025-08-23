package org.jsoup.internal;

import org.junit.Test;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link StringUtil} class.
 */
public class StringUtilTest {

    /**
     * Verifies that calling {@link StringUtil#padding(int, int)} with a negative width
     * throws an {@link IllegalArgumentException}. The method must not accept negative values
     * for the desired padding width.
     */
    @Test
    public void paddingWithNegativeWidthThrowsIllegalArgumentException() {
        // Arrange: Define an invalid input and the expected error message.
        int invalidWidth = -1;
        String expectedMessage = "width must be >= 0";

        try {
            // Act: Call the method with the invalid input.
            StringUtil.padding(invalidWidth, 10);
            fail("Expected an IllegalArgumentException to be thrown for negative width, but it was not.");
        } catch (IllegalArgumentException e) {
            // Assert: Verify that the exception has the expected message.
            assertEquals("The exception message should detail the validation error.", expectedMessage, e.getMessage());
        }
    }
}