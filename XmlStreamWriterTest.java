package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.junitpioneer.jupiter.DefaultLocale;

/**
 * Tests XmlStreamWriter.
 *
 * What we verify:
 * - When an XML declaration specifies an encoding, XmlStreamWriter writes using that encoding.
 * - When no XML declaration is present, XmlStreamWriter writes using its configured default encoding (or UTF-8 if none configured).
 * - The actual bytes written match the expected bytes for the chosen encoding.
 * - getEncoding() reports the detected/used encoding.
 */
class XmlStreamWriterTest {

    // Test content snippets with representative characters.
    private static final String TEXT_LATIN1 = "eacute: \u00E9";                         // French
    private static final String TEXT_LATIN7 = "alpha: \u03B1";                          // Greek
    private static final String TEXT_LATIN15 = "euro: \u20AC";                           // Euro
    private static final String TEXT_EUC_JP = "hiragana A: \u3042";                      // Japanese
    private static final String TEXT_UNICODE = TEXT_LATIN1 + ", " + TEXT_LATIN7 + ", " + TEXT_LATIN15 + ", " + TEXT_EUC_JP;

    // Charsets not present in StandardCharsets
    private static final Charset CS_EUC_JP = Charset.forName("EUC-JP");
    private static final Charset CS_ISO_8859_7 = Charset.forName("ISO-8859-7");
    private static final Charset CS_ISO_8859_15 = Charset.forName("ISO-8859-15");
    private static final Charset CS_CP1047 = Charset.forName("CP1047");

    /**
     * Writes the given XML through XmlStreamWriter and asserts:
     * - The writer detected/used the expectedCharset.
     * - The bytes written equal xml.getBytes(expectedCharset).
     */
    @SuppressWarnings("resource")
    private static void assertXmlContentWrittenWithEncoding(final String xml, final Charset expectedCharset, final Charset configuredDefault)
            throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final XmlStreamWriter writerRef;
        final XmlStreamWriter.Builder builder = XmlStreamWriter.builder().setOutputStream(out);
        if (configuredDefault != null) {
            builder.setCharset(configuredDefault);
        }
        try (XmlStreamWriter writer = builder.get()) {
            writerRef = writer; // Keep a reference so we can call getEncoding() after closing the try block
            writer.write(xml);
        }
        final byte[] actualBytes = out.toByteArray();
        final Charset writerDetected = Charset.forName(writerRef.getEncoding());

        assertEquals(expectedCharset, writerDetected, "Detected charset must match expected.");
        assertTrue(writerDetected.contains(expectedCharset), "Writer charset should contain expected charset: " + writerDetected.name());
        assertArrayEquals(xml.getBytes(expectedCharset), actualBytes, "Output bytes must match input encoded with expected charset.");
    }

    /**
     * Builds a minimal XML document for the given text. If declaredEncoding is non-null,
     * it is written into the XML declaration.
     */
    private static String xmlWithOptionalDecl(final String text, final Charset declaredEncoding) {
        final String xmlDecl = declaredEncoding == null
                ? "<?xml version=\"1.0\"?>"
                : "<?xml version=\"1.0\" encoding=\"" + declaredEncoding.name() + "\"?>";
        return xmlDecl + "\n<text>" + text + "</text>";
    }

    /**
     * Builds a minimal XML document for the given text using a literal encoding name in the XML declaration.
     * This overload is used to test locale-sensitive case handling (e.g., Turkish locale).
     */
    private static String xmlWithOptionalDeclName(final String text, final String encodingNameOrNull) {
        final String xmlDecl = encodingNameOrNull == null
                ? "<?xml version=\"1.0\"?>"
                : "<?xml version=\"1.0\" encoding=\"" + encodingNameOrNull + "\"?>";
        return xmlDecl + "\n<text>" + text + "</text>";
    }

    /**
     * Helper that computes the effective encoding based on:
     * - declaredEncoding (from XML declaration), else
     * - configuredDefault (writer default), else
     * - UTF-8 (XmlStreamWriter's default).
     */
    private static void assertXmlWriterBehavior(final String text, final Charset declaredEncoding, final Charset configuredDefault)
            throws IOException {
        final Charset expected = declaredEncoding != null
                ? declaredEncoding
                : (configuredDefault != null ? configuredDefault : StandardCharsets.UTF_8);
        final String xml = xmlWithOptionalDecl(text, declaredEncoding);
        assertXmlContentWrittenWithEncoding(xml, expected, configuredDefault);
    }

    /**
     * When no encoding is declared in the XML, the writer uses its default charset.
     */
    @ParameterizedTest
    @MethodSource("defaultCharsets")
    void usesConfiguredDefaultWhenNoXmlDeclaration(final Charset configuredDefault) throws IOException {
        assertXmlWriterBehavior(TEXT_UNICODE, null, configuredDefault);
    }

    /**
     * Declared encodings in the XML must drive the output encoding.
     */
    @ParameterizedTest
    @MethodSource("declaredEncodings")
    void usesDeclaredXmlEncoding(final String text, final Charset declaredEncoding) throws IOException {
        assertXmlWriterBehavior(text, declaredEncoding, null);
    }

    /**
     * Turkish locale has special casing rules for dotless/dotted 'i'.
     * Ensure lowercase encoding names in the XML declaration are handled correctly.
     */
    @Test
    @DefaultLocale(language = "tr")
    void acceptsLowerCaseEncodingNamesWithTurkishLocale_IO_557() throws IOException {
        // Build XML with literal lowercase encoding names in the declaration.
        final String xmlUtf8 = xmlWithOptionalDeclName(TEXT_UNICODE, "utf-8");
        assertXmlContentWrittenWithEncoding(xmlUtf8, StandardCharsets.UTF_8, null);

        final String xmlIso88591 = xmlWithOptionalDeclName(TEXT_LATIN1, "iso-8859-1");
        assertXmlContentWrittenWithEncoding(xmlIso88591, StandardCharsets.ISO_8859_1, null);

        final String xmlIso88597 = xmlWithOptionalDeclName(TEXT_LATIN7, "iso-8859-7");
        assertXmlContentWrittenWithEncoding(xmlIso88597, CS_ISO_8859_7, null);
    }

    /**
     * Writing nothing should be a no-op and not throw, both for deprecated and builder APIs.
     */
    @Test
    void writingEmptyStringsIsANoOp() throws IOException {
        exerciseEmptyWrites(out -> new XmlStreamWriter(out)); // Deprecated constructor
        exerciseEmptyWrites(out -> XmlStreamWriter.builder().setOutputStream(out).get()); // Builder API
    }

    /**
     * If there is no XML declaration at all, UTF-8 is used by default.
     */
    @Test
    void noXmlHeaderDefaultsToUtf8() throws IOException {
        final String xml = "<text>text with no XML header</text>";
        assertXmlContentWrittenWithEncoding(xml, StandardCharsets.UTF_8, null);
    }

    // ---------------------------------------------------------------------
    // Test data providers
    // ---------------------------------------------------------------------

    private static Stream<Charset> defaultCharsets() {
        // null means "use writer's built-in default", i.e., UTF-8.
        return Stream.of(
                null,
                StandardCharsets.UTF_8,
                StandardCharsets.UTF_16,
                StandardCharsets.UTF_16BE,
                StandardCharsets.ISO_8859_1
        );
    }

    private static Stream<org.junit.jupiter.params.provider.Arguments> declaredEncodings() {
        return Stream.of(
                org.junit.jupiter.params.provider.Arguments.of(TEXT_UNICODE, StandardCharsets.UTF_8),
                org.junit.jupiter.params.provider.Arguments.of(TEXT_UNICODE, StandardCharsets.UTF_16),
                org.junit.jupiter.params.provider.Arguments.of(TEXT_UNICODE, StandardCharsets.UTF_16BE),
                org.junit.jupiter.params.provider.Arguments.of(TEXT_UNICODE, StandardCharsets.UTF_16LE),
                org.junit.jupiter.params.provider.Arguments.of(TEXT_EUC_JP, CS_EUC_JP),
                org.junit.jupiter.params.provider.Arguments.of(TEXT_LATIN1, StandardCharsets.ISO_8859_1),
                org.junit.jupiter.params.provider.Arguments.of(TEXT_LATIN7, CS_ISO_8859_7),
                org.junit.jupiter.params.provider.Arguments.of(TEXT_LATIN15, CS_ISO_8859_15),
                org.junit.jupiter.params.provider.Arguments.of("simple text in EBCDIC", CS_CP1047)
        );
    }

    // ---------------------------------------------------------------------
    // Helpers
    // ---------------------------------------------------------------------

    @FunctionalInterface
    private interface WriterFactory {
        XmlStreamWriter create(ByteArrayOutputStream out) throws IOException;
    }

    private static void exerciseEmptyWrites(final WriterFactory factory) throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             XmlStreamWriter writer = factory.create(out)) {
            writer.flush();
            writer.write("");
            writer.flush();
            writer.write(".");
            writer.flush();
        }
    }
}