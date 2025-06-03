package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.*;
import java.nio.charset.Charset;
import java.nio.charset.UnsupportedCharsetException;

public class CharsetsTest {

    @Test(timeout = 4000)
    public void testToCharset_UnsupportedCharsetName() {
        // Test case to verify that an UnsupportedCharsetException is thrown when
        // an invalid or unsupported charset name is passed to Charsets.toCharset().

        // Arrange: Define an invalid charset name and a Charset instance.  We are using a class name here
        //  to ensure that the exception is not triggered by an illegal name format, but by an unsupported name.
        String invalidCharsetName = "org.apache.commons.io.Charsets";
        Charset existingCharset = Charset.forName("UTF-8");  // Using a valid charset for context.

        // Act & Assert: Attempt to convert the invalid charset name and assert that an UnsupportedCharsetException is thrown.
        try {
            Charsets.toCharset(invalidCharsetName, existingCharset);
            fail("Expected UnsupportedCharsetException was not thrown.");
        } catch (UnsupportedCharsetException e) {
            // Assert that the exception message contains the invalid charset name.
            assertEquals("org.apache.commons.io.Charsets", e.getCharsetName());
        }
    }
}