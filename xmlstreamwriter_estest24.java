package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Unit tests for {@link XmlStreamWriter}.
 */
public class XmlStreamWriterTest {

    /**
     * Verifies that the XmlStreamWriter constructor that accepts an OutputStream
     * uses UTF-8 as the default encoding when no encoding is specified.
     */
    @Test
    public void constructorWithOutputStreamShouldSetDefaultEncodingToUtf8() {
        // Arrange: Create a simple OutputStream and instantiate the writer.
        // A ByteArrayOutputStream is used as a lightweight, in-memory stream.
        final OutputStream outputStream = new ByteArrayOutputStream();
        final XmlStreamWriter writer = new XmlStreamWriter(outputStream);

        // Act: Retrieve the default encoding from the writer.
        final String defaultEncoding = writer.getDefaultEncoding();

        // Assert: Check if the default encoding is UTF-8 as expected.
        assertEquals("The default encoding should be UTF-8", "UTF-8", defaultEncoding);
    }
}