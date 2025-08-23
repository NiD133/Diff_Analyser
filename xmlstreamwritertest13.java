package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XmlStreamWriter} focusing on its ability to detect and use the
 * encoding specified in the XML prolog.
 */
public class XmlStreamWriterPrologEncodingTest {

    /** A composite string with characters from various languages to test Unicode support. */
    private static final String TEXT_UNICODE =
            "eacute: \u00E9" + ", " + // Latin-1
            "alpha: \u03B1" + ", " + // Latin-7
            "euro: \u20AC" + ", " +   // Latin-15
            "hiragana A: \u3042";     // EUC-JP

    /**
     * Tests that the writer correctly identifies the encoding from the XML prolog
     * (e.g., {@code <?xml version="1.0" encoding="UTF-8"?>}) and uses it to write
     * the output, overriding any default encoding that was configured.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void shouldWriteWithEncodingSpecifiedInXmlProlog() throws IOException {
        // Arrange
        final String expectedEncoding = StandardCharsets.UTF_8.name();
        final String xmlDocument = createXmlDocument(TEXT_UNICODE, expectedEncoding);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Use a different default encoding to ensure it is correctly ignored by the writer.
        final String defaultEncoding = StandardCharsets.US_ASCII.name();

        // Act
        final String detectedEncoding;
        try (XmlStreamWriter writer = XmlStreamWriter.builder()
                .setOutputStream(outputStream)
                .setCharset(defaultEncoding)
                .get()) {
            writer.write(xmlDocument);
            detectedEncoding = writer.getEncoding();
        }
        final byte[] actualBytes = outputStream.toByteArray();

        // Assert
        // 1. Verify the writer detected the correct encoding from the prolog.
        assertEquals(expectedEncoding, detectedEncoding,
                "Writer should detect the encoding specified in the XML prolog.");

        // 2. Verify the output bytes were written using the detected encoding.
        final byte[] expectedBytes = xmlDocument.getBytes(expectedEncoding);
        assertArrayEquals(expectedBytes, actualBytes,
                "Output bytes should be encoded using the encoding from the prolog.");
    }

    /**
     * Helper to create a simple XML document string with a specified encoding in the prolog.
     *
     * @param content The text content for the <text> element.
     * @param encoding The encoding to specify in the XML declaration.
     * @return A string representing the XML document.
     */
    private String createXmlDocument(final String content, final String encoding) {
        final String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        return xmlDeclaration + "\n<text>" + content + "</text>";
    }
}