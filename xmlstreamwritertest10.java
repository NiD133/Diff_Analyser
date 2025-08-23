package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XmlStreamWriter} focusing on its ability to detect and use the
 * encoding specified in the XML declaration.
 */
public class XmlStreamWriterEncodingDetectionTest {

    /**
     * French characters.
     */
    private static final String TEXT_LATIN1 = "eacute: \u00E9";

    /**
     * Greek characters.
     */
    private static final String TEXT_LATIN7 = "alpha: \u03B1";

    /**
     * Euro symbol.
     */
    private static final String TEXT_LATIN15 = "euro: \u20AC";

    /**
     * Japanese characters.
     */
    private static final String TEXT_EUC_JP = "hiragana A: \u3042";

    /**
     * A combination of characters from various character sets to test Unicode support.
     */
    private static final String TEXT_UNICODE = TEXT_LATIN1 + ", " + TEXT_LATIN7 + ", " + TEXT_LATIN15 + ", " + TEXT_EUC_JP;

    /**
     * Tests that the XmlStreamWriter correctly detects and uses the UTF-16BE encoding
     * when it is specified in the XML declaration, overriding any default encoding.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void writerShouldDetectAndUseUTF16BEWhenSpecifiedInXmlDeclaration() throws IOException {
        // Arrange
        final Charset expectedEncoding = StandardCharsets.UTF_16BE;
        final String xmlDocument = createXmlDocumentString(TEXT_UNICODE, expectedEncoding.name());
        final byte[] expectedOutput = xmlDocument.getBytes(expectedEncoding);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();

        // Act
        try (final XmlStreamWriter writer = XmlStreamWriter.builder().setOutputStream(outputStream).get()) {
            writer.write(xmlDocument);

            // Assert: Check that the writer detected the correct encoding from the XML declaration.
            // This assertion is inside the try-with-resources block to access the writer before it is closed.
            assertEquals(expectedEncoding.name(), writer.getEncoding(),
                "Writer should detect the encoding from the XML declaration.");
        }

        // Assert: Verify that the bytes written to the stream match the expected byte representation.
        final byte[] actualOutput = outputStream.toByteArray();
        assertArrayEquals(expectedOutput, actualOutput,
            "The written byte stream should match the expected bytes for the detected encoding.");
    }

    /**
     * Creates a simple XML document string with the given text content and an optional encoding declaration.
     *
     * @param text     The text content to embed in the XML.
     * @param encoding The encoding to specify in the XML declaration. If null, no encoding is specified.
     * @return A string representing the XML document.
     */
    private static String createXmlDocumentString(final String text, final String encoding) {
        final String xmlDeclaration = (encoding == null)
            ? "<?xml version=\"1.0\"?>"
            : "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        return xmlDeclaration + "\n<text>" + text + "</text>";
    }
}