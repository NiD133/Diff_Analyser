package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XmlStreamWriter}.
 * This class focuses on the writer's ability to detect and use the encoding
 * specified in the XML declaration.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that the writer correctly detects the encoding from the XML declaration
     * and writes the output in that encoding, even when it differs from the writer's
     * default encoding.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void shouldDetectAndWriteWithEncodingFromXmlDeclaration() throws IOException {
        // Arrange
        final String expectedEncoding = "CP1047"; // An EBCDIC encoding
        final String contentText = "simple text in EBCDIC";
        final String xml = createXmlWithEncoding(contentText, expectedEncoding);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // The writer is constructed with a default encoding of UTF-8.
        // It must override this default with the encoding found in the XML content.
        final XmlStreamWriter writer;

        // Act
        try (XmlStreamWriter managedWriter = XmlStreamWriter.builder()
                .setOutputStream(outputStream)
                .setCharset(StandardCharsets.UTF_8) // Set a different default encoding.
                .get()) {
            writer = managedWriter;
            writer.write(xml);
        }

        // Assert
        // 1. Verify the writer detected the correct encoding.
        assertEquals(expectedEncoding, writer.getEncoding(),
            "Writer should detect the encoding from the XML declaration.");

        // 2. Verify the output bytes are correctly encoded.
        final byte[] expectedBytes = xml.getBytes(expectedEncoding);
        assertArrayEquals(expectedBytes, outputStream.toByteArray(),
            "The output bytes should be encoded using the detected encoding.");
    }

    /**
     * Helper to create an XML string with a specified encoding in its declaration.
     *
     * @param text The text content for the <text> element.
     * @param encoding The encoding to specify in the XML declaration.
     * @return A complete XML string.
     */
    private static String createXmlWithEncoding(final String text, final String encoding) {
        final String xmlDecl = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        return xmlDecl + "\n<text>" + text + "</text>";
    }
}