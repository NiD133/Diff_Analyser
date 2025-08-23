package org.apache.commons.io.output;

import org.junit.Test;

import java.io.OutputStream;
import java.nio.charset.IllegalCharsetNameException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

/**
 * Tests for the {@link XmlStreamWriter} constructor.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that the constructor throws an {@link IllegalCharsetNameException}
     * when provided with a string that is not a valid character set name.
     */
    @Test
    public void constructorShouldThrowExceptionForInvalidCharsetName() {
        // Arrange: Define an invalid charset name.
        // According to the Charset documentation, names cannot contain special characters like '<', '?', or '!'.
        final String invalidCharsetName = "<?!wml";

        // Act & Assert: Attempt to create the writer and verify the exception.
        try {
            new XmlStreamWriter((OutputStream) null, invalidCharsetName);
            fail("Expected an IllegalCharsetNameException to be thrown for invalid charset name.");
        } catch (final IllegalCharsetNameException e) {
            // The exception message should be the invalid name itself.
            assertEquals(invalidCharsetName, e.getMessage());
        }
    }
}