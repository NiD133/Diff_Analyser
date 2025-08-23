package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XmlStreamWriter} focusing on encoding detection and writing.
 */
class XmlStreamWriterEncodingTest {

    /**
     * French text with a character specific to Latin-1.
     */
    private static final String TEXT_LATIN1 = "eacute: \u00E9";

    /**
     * Greek text with a character specific to Latin-7.
     */
    private static final String TEXT_LATIN7 = "alpha: \u03B1";

    /**
     * Text with the Euro symbol, specific to Latin-15.
     */
    private static final String TEXT_LATIN15 = "euro: \u20AC";

    /**
     * Japanese text with a Hiragana character.
     */
    private static final String TEXT_EUC_JP = "hiragana A: \u3042";

    /**
     * A composite string with characters from various encodings.
     */
    private static final String TEXT_UNICODE = TEXT_LATIN1 + ", " + TEXT_LATIN7 + ", " + TEXT_LATIN15 + ", " + TEXT_EUC_JP;

    @Test
    @DisplayName("Writer should detect and use EUC-JP encoding when it is declared in the XML prolog")
    void shouldDetectAndWriteWithEucJpEncodingWhenDeclaredInXmlProlog() throws IOException {
        // Given Japanese text and an "EUC-JP" encoding declared in the XML prolog.
        // When the XML is written using XmlStreamWriter.
        // Then the writer should detect and use the EUC-JP encoding for the output stream.
        assertWriterUsesCorrectEncoding(TEXT_EUC_JP, "EUC-JP", null);
    }

    /**
     * A comprehensive assertion helper that:
     * 1. Creates an XML string with the given text and encoding declaration.
     * 2. Writes this XML using XmlStreamWriter.
     * 3. Asserts that the writer detected the correct encoding.
     * 4. Asserts that the resulting byte array is correctly encoded.
     *
     * @param payloadText The text content to embed in the XML.
     * @param declaredEncoding The encoding to specify in the XML prolog (e.g., "UTF-8"). Can be null.
     * @param writerDefaultEncoding The default encoding to configure for the XmlStreamWriter. Can be null.
     * @throws IOException If an I/O error occurs.
     */
    private static void assertWriterUsesCorrectEncoding(final String payloadText, final String declaredEncoding, final String writerDefaultEncoding) throws IOException {
        // ARRANGE
        final String xmlContent = createXmlWithProlog(payloadText, declaredEncoding);
        final String expectedEncoding = determineExpectedEncoding(declaredEncoding, writerDefaultEncoding);
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final XmlStreamWriter writer;

        // ACT: Write the XML. The try-with-resources ensures the writer is closed.
        // We keep a reference to the writer to inspect its state after closing.
        try (XmlStreamWriter managedWriter = XmlStreamWriter.builder()
                .setOutputStream(outputStream)
                .setCharset(writerDefaultEncoding)
                .get()) {
            writer = managedWriter;
            writer.write(xmlContent);
        }

        // ASSERT
        // 1. Verify the writer detected the correct encoding from the XML prolog.
        assertEquals(expectedEncoding, writer.getEncoding(), "Writer should have detected the correct encoding.");

        // 2. Verify the output bytes match the expected encoding.
        final byte[] expectedBytes = xmlContent.getBytes(expectedEncoding);
        final byte[] actualBytes = outputStream.toByteArray();
        assertArrayEquals(expectedBytes, actualBytes, "The written byte stream should match the expected encoding.");
    }

    /**
     * Determines the encoding that the writer is expected to use based on the
     * declared encoding in the XML and the writer's default setting.
     * The declared encoding in the XML prolog takes precedence.
     */
    private static String determineExpectedEncoding(final String declaredEncoding, final String writerDefaultEncoding) {
        if (declaredEncoding != null) {
            return declaredEncoding;
        }
        if (writerDefaultEncoding != null) {
            return writerDefaultEncoding;
        }
        return StandardCharsets.UTF_8.name();
    }

    /**
     * Creates an XML document string with an optional encoding declaration in the prolog.
     *
     * @param text The text content for the <text> element.
     * @param encoding The encoding to declare in the XML prolog. If null, no encoding is declared.
     * @return A well-formed XML string.
     */
    private static String createXmlWithProlog(final String text, final String encoding) {
        final String xmlDecl;
        if (encoding != null) {
            xmlDecl = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        } else {
            xmlDecl = "<?xml version=\"1.0\"?>";
        }
        return xmlDecl + "\n<text>" + text + "</text>";
    }
}