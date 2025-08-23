package org.jsoup.helper;

import org.junit.Test;

import java.util.IllegalFormatConversionException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

public class Validate_ESTestTest14 extends Validate_ESTest_scaffolding {

    /**
     * Tests that `ensureNotNull` throws an `IllegalFormatConversionException`
     * when the object is null and the provided error message format string is invalid
     * for the given arguments.
     *
     * This verifies that exceptions occurring during the formatting of the failure
     * message are correctly propagated.
     */
    @Test(timeout = 4000)
    public void ensureNotNullThrowsExceptionForMismatchedMessageFormatSpecifier() {
        // Arrange: Define a message format string with a specifier (%x for a hex integer)
        // and provide an argument of a mismatched type (a String).
        String messageFormat = "Object should not be null. Details: %x";
        Object[] formatArgs = {"this is a string, not an integer"};

        try {
            // Act: Call the method with a null object to trigger the validation.
            // The exception is expected to occur during the formatting of the failure message.
            Validate.ensureNotNull(null, messageFormat, formatArgs);

            // Assert: If this line is reached, the test fails because no exception was thrown.
            fail("Expected an IllegalFormatConversionException to be thrown.");
        } catch (IllegalFormatConversionException e) {
            // Assert: Verify that the correct exception was thrown and that its message
            // matches the one from the underlying Java Formatter.
            assertEquals("x != java.lang.String", e.getMessage());
        }
    }
}