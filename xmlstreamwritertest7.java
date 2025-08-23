package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XmlStreamWriter} focusing on encoding detection.
 */
class XmlStreamWriterTest {

    /**
     * Greek text containing characters specific to the ISO-8859-7 character set.
     */
    private static final String GREEK_TEXT_LATIN7 = "alpha: \u03B1";

    /**
     * Creates a simple XML document string with the given content and encoding declaration.
     *
     * @param content  The text content for the <text> element.
     * @param encoding The encoding to specify in the XML declaration.
     * @return A string representing the XML document.
     */
    private String createXmlWithEncoding(final String content, final String encoding) {
        final String xmlDeclaration = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        return xmlDeclaration + "\n<text>" + content + "</text>";
    }

    @Test
    @DisplayName("Writer should detect and use encoding from the XML declaration")
    void writerShouldDetectAndUseEncodingFromXmlDeclaration() throws IOException {
        // ARRANGE
        final String expectedEncoding = "ISO-8859-7";
        final String xmlString = createXmlWithEncoding(GREEK_TEXT_LATIN7, expectedEncoding);
        final byte[] expectedBytes = xmlString.getBytes(expectedEncoding);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        final String detectedEncoding;
        final byte[] actualBytes;

        // ACT
        // The writer is created with a default encoding (UTF-8), but it should
        // detect and switch to the encoding specified in the XML declaration ("ISO-8859-7").
        try (XmlStreamWriter writer = XmlStreamWriter.builder()
                .setOutputStream(outputStream)
                .setCharset(StandardCharsets.UTF_8) // Set a different default to ensure detection works.
                .get()) {
            writer.write(xmlString);
            // Capture the detected encoding before the writer is closed.
            detectedEncoding = writer.getEncoding();
        }
        actualBytes = outputStream.toByteArray();

        // ASSERT
        assertAll("Verify encoding detection and output",
            () -> assertEquals(expectedEncoding, detectedEncoding,
                "Should detect the encoding specified in the XML declaration."),
            () -> assertArrayEquals(expectedBytes, actualBytes,
                "The output bytes should be encoded with the detected character set.")
        );
    }
}