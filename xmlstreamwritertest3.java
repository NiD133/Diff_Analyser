package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;

/**
 * Tests for {@link XmlStreamWriter}.
 *
 * <p>
 * This test suite focuses on two main areas:
 * </p>
 * <ol>
 *   <li>Verifying that the writer correctly detects and applies the encoding specified in the XML declaration.</li>
 *   <li>Ensuring the writer's stability and correctness when handling edge cases like empty writes and multiple flushes.</li>
 * </ol>
 */
public class XmlStreamWriterTest {

    /** French text with a Latin-1 character. */
    private static final String TEXT_LATIN1 = "eacute: \u00E9";

    /** Greek text with a Latin-7 character. */
    private static final String TEXT_LATIN7 = "alpha: \u03B1";

    /** Text with the Euro symbol, requiring Latin-15/ISO-8859-15. */
    private static final String TEXT_LATIN15 = "euro: \u20AC";

    /** Japanese text. */
    private static final String TEXT_EUC_JP = "hiragana A: \u3042";

    /**
     * Tests that a sequence of writes and flushes using a writer from the
     * legacy constructor produces the correct final output.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void writeSequence_withLegacyConstructor_writesCorrectly() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             XmlStreamWriter writer = new XmlStreamWriter(out)) {
            // The writer defaults to UTF-8.
            // This sequence verifies that multiple flushes and an empty write do not
            // corrupt the stream, and subsequent content is written correctly.
            writer.flush();
            writer.write("");
            writer.flush();
            writer.write(".");
            writer.flush();

            // The final output should be "." encoded in UTF-8.
            assertArrayEquals(".".getBytes(StandardCharsets.UTF_8), out.toByteArray());
        }
    }

    /**
     * Tests that a sequence of writes and flushes using a writer from the
     * builder produces the correct final output.
     *
     * @throws IOException if an I/O error occurs.
     */
    @Test
    void writeSequence_withBuilder_writesCorrectly() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             XmlStreamWriter writer = XmlStreamWriter.builder().setOutputStream(out).get()) {
            // The builder defaults to UTF-8.
            // This sequence verifies that multiple flushes and an empty write do not
            // corrupt the stream, and subsequent content is written correctly.
            writer.flush();
            writer.write("");
            writer.flush();
            writer.write(".");
            writer.flush();

            // The final output should be "." encoded in UTF-8.
            assertArrayEquals(".".getBytes(StandardCharsets.UTF_8), out.toByteArray());
        }
    }

    /**
     * Tests that an XML document with an ISO-8859-1 declaration is written correctly.
     */
    @Test
    void write_withLatin1Declaration_detectsCorrectEncoding() throws IOException {
        checkXmlWriter(TEXT_LATIN1, "ISO-8859-1");
    }

    /**
     * Tests that an XML document with an ISO-8859-7 declaration is written correctly.
     */
    @Test
    void write_withLatin7Declaration_detectsCorrectEncoding() throws IOException {
        checkXmlWriter(TEXT_LATIN7, "ISO-8859-7");
    }

    /**
     * Tests that an XML document with an ISO-8859-15 declaration is written correctly.
     */
    @Test
    void write_withLatin15Declaration_detectsCorrectEncoding() throws IOException {
        checkXmlWriter(TEXT_LATIN15, "ISO-8859-15");
    }

    /**
     * Tests that an XML document with a EUC-JP declaration is written correctly.
     */
    @Test
    void write_withEucJpDeclaration_detectsCorrectEncoding() throws IOException {
        checkXmlWriter(TEXT_EUC_JP, "EUC-JP");
    }

    /**
     * Tests that writing XML without an encoding declaration uses the writer's
     * default encoding (UTF-8 by default).
     */
    @Test
    void write_withoutDeclaration_usesDefaultEncoding() throws IOException {
        checkXmlWriter(TEXT_LATIN1, null, "UTF-8");
    }

    /**
     * Creates an XML string, writes it using XmlStreamWriter, and verifies that
     * the detected encoding and byte output are correct.
     *
     * @param text The text content for the XML element.
     * @param encoding The encoding to specify in the XML declaration (e.g., "UTF-8").
     * @param defaultEncoding The default encoding to configure for the writer.
     * @throws IOException if an I/O error occurs.
     */
    private static void checkXmlWriter(final String text, final String encoding, final String defaultEncoding) throws IOException {
        final String xml = createXmlContent(text, encoding);

        // Determine the encoding we expect the writer to ultimately use.
        // If the XML declares an encoding, that should be used.
        // Otherwise, it should fall back to the writer's default encoding.
        final String expectedEncoding = encoding != null ? encoding : defaultEncoding;

        checkXmlContent(xml, expectedEncoding, defaultEncoding);
    }

    /**
     * Overloaded helper for tests where the writer's default encoding is not explicitly set.
     */
    private static void checkXmlWriter(final String text, final String encoding) throws IOException {
        checkXmlWriter(text, encoding, StandardCharsets.UTF_8.name());
    }

    /**
     * Writes an XML string to an in-memory stream and asserts that the detected
     * encoding and resulting bytes are as expected.
     *
     * @param xml The full XML string to write.
     * @param expectedEncoding The encoding that the writer should detect and use.
     * @param writerDefaultEncoding The default encoding to configure for the writer.
     * @throws IOException if an I/O error occurs.
     */
    private static void checkXmlContent(final String xml, final String expectedEncoding, final String writerDefaultEncoding) throws IOException {
        final byte[] writtenBytes;

        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             XmlStreamWriter writer = XmlStreamWriter.builder()
                 .setOutputStream(out)
                 .setCharset(writerDefaultEncoding)
                 .get()) {

            writer.write(xml);

            // The encoding is detected upon the first write. Assert it's correct.
            assertEquals(expectedEncoding, writer.getEncoding(), "Writer should have detected the correct encoding.");

            // The try-with-resources statement will auto-close the writer, which flushes all content.
            writtenBytes = out.toByteArray();
        }

        // Assert that the raw bytes written to the stream match the XML string
        // converted to bytes using the expected encoding.
        final byte[] expectedBytes = xml.getBytes(expectedEncoding);
        assertArrayEquals(expectedBytes, writtenBytes, "The written byte content should match the expected bytes.");
    }

    /**
     * Creates a simple XML document string with the given text content and encoding declaration.
     *
     * @param text The text content for the <text> element.
     * @param encoding The encoding to include in the XML declaration. If null, no encoding attribute is added.
     * @return A string containing the XML document.
     */
    private static String createXmlContent(final String text, final String encoding) {
        final String xmlDecl = encoding != null
            ? "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>"
            : "<?xml version=\"1.0\"?>";
        return xmlDecl + "\n<text>" + text + "</text>";
    }
}