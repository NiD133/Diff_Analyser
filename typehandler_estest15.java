package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

/**
 * Tests for the {@link TypeHandler} class.
 */
public class TypeHandlerTest {

    /**
     * Verifies that createDate() throws an IllegalArgumentException when provided
     * with a string that does not conform to a recognizable date format.
     */
    @Test
    public void createDate_withInvalidFormat_shouldThrowIllegalArgumentException() {
        // Arrange: Define an input string that is not a valid date.
        final String invalidDateString = "w2E%~v5+#";

        // Act & Assert: Expect an IllegalArgumentException to be thrown.
        // The modern assertThrows is preferred for its clarity and conciseness.
        IllegalArgumentException thrown = assertThrows(
            IllegalArgumentException.class,
            () -> TypeHandler.createDate(invalidDateString)
        );

        // Assert: Further inspect the exception to ensure it's for the right reason.
        // The underlying implementation is expected to wrap a ParseException.
        String expectedMessageContent = "Unparseable date: \"" + invalidDateString + "\"";
        assertTrue(
            "Exception message should contain details about the parsing failure.",
            thrown.getMessage().contains(expectedMessageContent)
        );
    }
}