package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XmlStreamWriter}.
 * <p>
 * This class focuses on verifying that the writer correctly detects and uses the
 * encoding specified in an XML declaration.
 * </p>
 */
public class XmlStreamWriterTest {

    /**
     * A sample text containing a French character (é), representable in ISO-8859-1 (Latin-1).
     */
    private static final String TEXT_LATIN1 = "eacute: \u00E9";

    /**
     * Creates an XML document string with a specified encoding in its declaration.
     *
     * @param text The content for the {@code <text>} element.
     * @param encodingName The encoding name to specify in the XML declaration.
     * @return A string representing the XML document.
     */
    private static String createXmlWithEncoding(final String text, final String encodingName) {
        final String xmlDecl = "<?xml version=\"1.0\" encoding=\"" + encodingName + "\"?>";
        return xmlDecl + "\n<text>" + text + "</text>";
    }

    @Test
    void write_whenXmlDeclaresIso8859_1_usesDetectedEncoding() throws IOException {
        // Arrange
        final Charset declaredEncoding = StandardCharsets.ISO_8859_1;
        final String xmlContent = createXmlWithEncoding(TEXT_LATIN1, declaredEncoding.name());
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // The XmlStreamWriter is created with its default encoding (UTF-8), but it should
        // detect the encoding from the XML declaration and switch to it.
        final XmlStreamWriter writer = XmlStreamWriter.builder()
                .setOutputStream(outputStream)
                .get(); // Uses UTF-8 as default unless specified otherwise.

        // Act
        try (writer) {
            writer.write(xmlContent);
        }

        // Assert
        // 1. Verify the writer detected the correct encoding from the declaration.
        assertEquals(declaredEncoding.name(), writer.getEncoding());

        // 2. Verify the output bytes are correct for the detected encoding.
        final byte[] expectedBytes = xmlContent.getBytes(declaredEncoding);
        final byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes);
    }
}