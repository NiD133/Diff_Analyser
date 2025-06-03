package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.charset.IllegalCharsetNameException;

public class Charsets_InvalidCharsetName_Test {

    @Test(expected = IllegalCharsetNameException.class)
    public void testInvalidCharsetNameThrowsException() {
        // This test verifies that providing an invalid charset name to Charsets.toCharset()
        // throws the expected IllegalCharsetNameException.  The charset name "?" is deliberately invalid.

        // Attempt to convert the invalid charset name to a Charset.
        // This should throw an exception.
        try {
            Charsets.toCharset("?");
        } catch (IllegalCharsetNameException e) {
            // The exception is expected, so we re-throw it to satisfy the @Test(expected=...) annotation.
            throw e;
        }

        // If we reach this point, the test has failed because no exception was thrown.
        // The @Test(expected=...) annotation would also cause the test to fail in this case.
        fail("Expected IllegalCharsetNameException was not thrown.");
    }
}