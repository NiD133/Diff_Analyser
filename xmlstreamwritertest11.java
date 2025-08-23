package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XmlStreamWriter}.
 * This test focuses on the writer's ability to detect and use the encoding
 * specified in the XML declaration.
 */
public class XmlStreamWriterTest {

    /**
     * French text with a Latin-1 character.
     */
    private static final String TEXT_LATIN1 = "eacute: \u00E9";

    /**
     * Greek text with a Latin-7 character.
     */
    private static final String TEXT_LATIN7 = "alpha: \u03B1";

    /**
     * Text with the Euro symbol, a Latin-15 character.
     */
    private static final String TEXT_LATIN15 = "euro: \u20AC";

    /**
     * Japanese text with a EUC-JP character.
     */
    private static final String TEXT_EUC_JP = "hiragana A: \u3042";

    /**
     * A composite string with characters from various encodings to test Unicode support.
     */
    private static final String TEXT_UNICODE = TEXT_LATIN1 + ", " + TEXT_LATIN7 + ", " + TEXT_LATIN15 + ", " + TEXT_EUC_JP;

    /**
     * Tests that the XmlStreamWriter correctly detects and uses the encoding specified
     * in the XML declaration, overriding any default encoding.
     */
    @Test
    void shouldUseEncodingFromXmlDeclaration() throws IOException {
        // Arrange
        final String expectedEncoding = StandardCharsets.UTF_16.name();
        final String xmlContent = createXmlWithDeclaration(TEXT_UNICODE, expectedEncoding);

        // Act & Assert
        // The default encoding (UTF-8) should be ignored in favor of the one in the XML declaration (UTF-16).
        assertXmlIsWrittenWithCorrectEncoding(xmlContent, expectedEncoding, StandardCharsets.UTF_8.name());
    }

    /**
     * Creates an XML string with a standard declaration including the specified encoding.
     *
     * @param text     The text content for the XML.
     * @param encoding The encoding to specify in the XML declaration.
     * @return A complete XML string.
     */
    private static String createXmlWithDeclaration(final String text, final String encoding) {
        final String xmlDecl = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        return xmlDecl + "\n<text>" + text + "</text>";
    }

    /**
     * Asserts that writing an XML string with an {@link XmlStreamWriter} results in the correct byte output
     * for the expected encoding.
     *
     * @param xmlString         The full XML string to write, including the declaration.
     * @param expectedEncoding  The encoding that the writer is expected to detect and use.
     * @param defaultEncoding   The default encoding to configure the writer with.
     * @throws IOException If an I/O error occurs.
     */
    private static void assertXmlIsWrittenWithCorrectEncoding(final String xmlString, final String expectedEncoding,
            final String defaultEncoding) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final String detectedEncoding;

        // Act: Write the XML string using XmlStreamWriter and capture the output.
        try (XmlStreamWriter writer = XmlStreamWriter.builder()
                .setOutputStream(out)
                .setCharset(defaultEncoding)
                .get()) {
            writer.write(xmlString);
            // Capture the encoding detected by the writer before it's closed.
            detectedEncoding = writer.getEncoding();
        }

        final byte[] writtenBytes = out.toByteArray();
        final byte[] expectedBytes = xmlString.getBytes(expectedEncoding);

        // Assert
        assertEquals(expectedEncoding, detectedEncoding,
                "Writer should detect the encoding from the XML declaration.");

        assertArrayEquals(expectedBytes, writtenBytes,
                "The output bytes should match the XML string encoded with the declared encoding.");
    }
}