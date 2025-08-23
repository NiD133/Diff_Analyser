package org.apache.commons.io.output;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

import org.junit.jupiter.api.Test;
import org.junitpioneer.jupiter.DefaultLocale;

/**
 * Unit tests for {@link XmlStreamWriter}.
 */
class XmlStreamWriterTest {

    // Sample text in various encodings
    private static final String TEXT_FRENCH = "eacute: \u00E9"; // Latin-1
    private static final String TEXT_GREEK = "alpha: \u03B1"; // Latin-7
    private static final String TEXT_EURO = "euro: \u20AC"; // Latin-15
    private static final String TEXT_JAPANESE = "hiragana A: \u3042"; // EUC-JP
    private static final String TEXT_UNICODE = TEXT_FRENCH + ", " + TEXT_GREEK + ", " + TEXT_EURO + ", " + TEXT_JAPANESE;

    /**
     * Verifies that the XML content is correctly written with the specified encoding.
     */
    private static void verifyXmlContent(final String xml, final String expectedEncoding, final String defaultEncoding) throws IOException {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        XmlStreamWriter writerCheck;

        try (XmlStreamWriter writer = XmlStreamWriter.builder().setOutputStream(outputStream).setCharset(defaultEncoding).get()) {
            writerCheck = writer;
            writer.write(xml);
        }

        byte[] actualXmlContent = outputStream.toByteArray();
        Charset expectedCharset = Charset.forName(expectedEncoding);
        Charset actualCharset = Charset.forName(writerCheck.getEncoding());

        assertEquals(expectedCharset, actualCharset, "Charset mismatch");
        assertTrue(actualCharset.contains(expectedCharset), "Charset containment mismatch");
        assertArrayEquals(xml.getBytes(expectedEncoding), actualXmlContent, "XML content mismatch");
    }

    /**
     * Helper method to test XML writing with specified text and encoding.
     */
    private static void testXmlWriter(final String text, final String encoding) throws IOException {
        testXmlWriter(text, encoding, null);
    }

    /**
     * Helper method to test XML writing with specified text, encoding, and default encoding.
     */
    private static void testXmlWriter(final String text, final String encoding, final String defaultEncoding) throws IOException {
        String xmlContent = createXmlDeclaration(text, encoding);
        String effectiveEncoding = encoding != null ? encoding : (defaultEncoding != null ? defaultEncoding : StandardCharsets.UTF_8.name());
        verifyXmlContent(xmlContent, effectiveEncoding, defaultEncoding);
    }

    /**
     * Creates an XML declaration with the specified text and encoding.
     */
    private static String createXmlDeclaration(final String text, final String encoding) {
        String xmlDeclaration = "<?xml version=\"1.0\"?>";
        if (encoding != null) {
            xmlDeclaration = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        }
        return xmlDeclaration + "\n<text>" + text + "</text>";
    }

    @Test
    void testDefaultEncoding() throws IOException {
        testXmlWriter(TEXT_UNICODE, null, null);
        testXmlWriter(TEXT_UNICODE, null, StandardCharsets.UTF_8.name());
        testXmlWriter(TEXT_UNICODE, null, StandardCharsets.UTF_16.name());
        testXmlWriter(TEXT_UNICODE, null, StandardCharsets.UTF_16BE.name());
        testXmlWriter(TEXT_UNICODE, null, StandardCharsets.ISO_8859_1.name());
    }

    @Test
    void testEBCDICEncoding() throws IOException {
        testXmlWriter("simple text in EBCDIC", "CP1047");
    }

    @Test
    void testEmptyContent() throws IOException {
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             XmlStreamWriter writer = new XmlStreamWriter(out)) {
            writer.flush();
            writer.write("");
            writer.flush();
            writer.write(".");
            writer.flush();
        }
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
             XmlStreamWriter writer = XmlStreamWriter.builder().setOutputStream(out).get()) {
            writer.flush();
            writer.write("");
            writer.flush();
            writer.write(".");
            writer.flush();
        }
    }

    @Test
    void testEUC_JPEncoding() throws IOException {
        testXmlWriter(TEXT_JAPANESE, "EUC-JP");
    }

    @Test
    void testLatin15Encoding() throws IOException {
        testXmlWriter(TEXT_EURO, "ISO-8859-15");
    }

    @Test
    void testLatin1Encoding() throws IOException {
        testXmlWriter(TEXT_FRENCH, StandardCharsets.ISO_8859_1.name());
    }

    @Test
    void testLatin7Encoding() throws IOException {
        testXmlWriter(TEXT_GREEK, "ISO-8859-7");
    }

    @Test
    @DefaultLocale(language = "tr")
    void testLowerCaseEncodingWithTurkishLocale() throws IOException {
        testXmlWriter(TEXT_UNICODE, "utf-8");
        testXmlWriter(TEXT_FRENCH, "iso-8859-1");
        testXmlWriter(TEXT_GREEK, "iso-8859-7");
    }

    @Test
    void testNoXmlHeader() throws IOException {
        verifyXmlContent("<text>text with no XML header</text>", StandardCharsets.UTF_8.name(), null);
    }

    @Test
    void testUTF16BEEncoding() throws IOException {
        testXmlWriter(TEXT_UNICODE, StandardCharsets.UTF_16BE.name());
    }

    @Test
    void testUTF16Encoding() throws IOException {
        testXmlWriter(TEXT_UNICODE, StandardCharsets.UTF_16.name());
    }

    @Test
    void testUTF16LEEncoding() throws IOException {
        testXmlWriter(TEXT_UNICODE, StandardCharsets.UTF_16LE.name());
    }

    @Test
    void testUTF8Encoding() throws IOException {
        testXmlWriter(TEXT_UNICODE, StandardCharsets.UTF_8.name());
    }
}