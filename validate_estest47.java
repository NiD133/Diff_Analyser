package org.jsoup.helper;

import org.junit.Test;
import java.util.MissingFormatArgumentException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link Validate} utility class.
 */
public class ValidateTest {

    /**
     * Verifies that Validate.fail() throws a MissingFormatArgumentException
     * when the format string expects an argument but none is provided.
     * This behavior is inherited from the underlying String.format() method.
     */
    @Test
    public void failWithMismatchedArgumentsThrowsMissingFormatArgumentException() {
        // Arrange: A message template that requires one argument.
        String messageWithSpecifier = "The parameter '%s' must not be null.";
        Object[] noArguments = new Object[0];

        try {
            // Act: Call the method with a mismatched number of arguments.
            Validate.fail(messageWithSpecifier, noArguments);
            
            // If this line is reached, the test has failed because no exception was thrown.
            fail("A MissingFormatArgumentException should have been thrown.");
        } catch (MissingFormatArgumentException e) {
            // Assert: Verify the exception is the one we expect.
            // The message should identify the problematic format specifier.
            assertEquals("Format specifier '%s'", e.getMessage());
        }
    }
}