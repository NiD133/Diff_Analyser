package org.apache.commons.io.output;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.ByteArrayOutputStream;
import java.io.OutputStream;

/**
 * Tests for {@link XmlStreamWriter}.
 * This class focuses on verifying the default encoding behavior.
 */
public class XmlStreamWriter_ESTestTest21 { // Note: Original class name kept for context

    /**
     * Tests that the XmlStreamWriter defaults to UTF-8 encoding when no specific
     * encoding is provided in the constructor.
     */
    @Test
    public void constructorWithOutputStreamShouldDefaultToUtf8Encoding() {
        // Arrange: Create a dummy output stream.
        OutputStream outputStream = new ByteArrayOutputStream();

        // Act: Instantiate XmlStreamWriter with only the output stream.
        XmlStreamWriter writer = new XmlStreamWriter(outputStream);

        // Assert: Verify that the default encoding is set to "UTF-8".
        assertEquals("UTF-8", writer.getDefaultEncoding());
    }
}