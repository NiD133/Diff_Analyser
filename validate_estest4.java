package org.jsoup.helper;

import org.junit.Test;
import java.util.UnknownFormatConversionException;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Test suite for {@link Validate}.
 * This class contains an improved version of a test that was originally auto-generated.
 */
public class ValidateTest {

    /**
     * Tests that {@link Validate#fail(String, Object...)} throws an
     * {@link UnknownFormatConversionException} when provided with a message
     * string containing an invalid format specifier.
     *
     * This test ensures that the underlying exception from Java's string formatting
     * is correctly propagated to the caller.
     */
    @Test
    public void failWithInvalidFormatStringThrowsException() {
        // Arrange: A format string with an invalid conversion specifier '%m'.
        // The rest of the string is arbitrary but was part of the original generated test.
        String invalidFormatString = "w%(7ml:";
        Object[] noArguments = new Object[0];

        // Act & Assert
        try {
            Validate.fail(invalidFormatString, noArguments);
            fail("Expected an UnknownFormatConversionException to be thrown, but no exception was thrown.");
        } catch (UnknownFormatConversionException e) {
            // Verify that the exception is the one we expect, with the correct message.
            assertEquals("Conversion = 'm'", e.getMessage());
        } catch (Exception e) {
            // Fail the test if a different, unexpected exception is thrown.
            fail("Expected an UnknownFormatConversionException, but caught " + e.getClass().getName());
        }
    }
}