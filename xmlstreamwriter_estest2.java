package org.apache.commons.io.output;

import org.junit.Test;
import java.io.ByteArrayOutputStream;
import static org.junit.Assert.assertEquals;

/**
 * Tests for {@link XmlStreamWriter}.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that the constructor correctly defaults to UTF-8 when a null encoding
     * is specified. This behavior is important for ensuring predictable output
     * when an encoding is not explicitly provided.
     */
    @Test
    public void shouldDefaultToUtf8WhenConstructedWithNullEncoding() {
        // Arrange
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        String nullDefaultEncoding = null;

        // Act
        // Using the deprecated constructor to test its specific behavior.
        XmlStreamWriter writer = new XmlStreamWriter(outputStream, nullDefaultEncoding);

        // Assert
        assertEquals("The default encoding should be UTF-8", "UTF-8", writer.getDefaultEncoding());
    }
}