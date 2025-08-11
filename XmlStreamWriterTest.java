/*
 * Licensed to the Apache Software Foundation (ASF) under one or more
 * contributor license agreements.  See the NOTICE file distributed with
 * this work for additional information regarding copyright ownership.
 * The ASF licenses this file to You under the Apache License, Version 2.0
 * (the "License"); you may not use this file except in compliance with
 * the License.  You may obtain a copy of the License at
 *
 *      https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
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
 * Tests {@link XmlStreamWriter}.
 */
class XmlStreamWriterTest {

    // Sample text in different languages/encodings
    private static final String TEXT_LATIN1 = "eacute: \u00E9";        // French
    private static final String TEXT_LATIN7 = "alpha: \u03B1";          // Greek
    private static final String TEXT_LATIN15 = "euro: \u20AC";          // Euro symbol
    private static final String TEXT_EUC_JP = "hiragana A: \u3042";     // Japanese
    private static final String TEXT_UNICODE = TEXT_LATIN1 + ", " + TEXT_LATIN7 
        + ", " + TEXT_LATIN15 + ", " + TEXT_EUC_JP; // Combined unicode text

    // XML declaration patterns
    private static final String XML_DECLARATION_NO_ENCODING = "<?xml version=\"1.0\"?>";
    private static final String XML_DECLARATION_WITH_ENCODING = "<?xml version=\"1.0\" encoding=\"%s\"?>";

    /**
     * Verifies that XML content is written with the expected encoding.
     *
     * @param xml XML content to verify
     * @param expectedEncoding Expected encoding name (e.g., "UTF-8")
     * @param defaultEncoding Default encoding to use if not specified in XML (nullable)
     */
    @SuppressWarnings("resource")
    private static void verifyXmlContentAndEncoding(final String xml, 
                                                    final String expectedEncoding, 
                                                    final String defaultEncoding) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final XmlStreamWriter xmlWriter;
        try (XmlStreamWriter writer = XmlStreamWriter.builder()
                .setOutputStream(out)
                .setCharset(defaultEncoding)
                .get()) {
            xmlWriter = writer;
            writer.write(xml);
        }
        final byte[] xmlBytes = out.toByteArray();
        final Charset expectedCharset = Charset.forName(expectedEncoding);
        final Charset actualCharset = Charset.forName(xmlWriter.getEncoding());

        // Verify detected encoding matches expected
        assertEquals(expectedCharset, actualCharset, 
            "Detected charset should match expected");
        
        // Verify the detected charset can encode the expected charset
        assertTrue(actualCharset.contains(expectedCharset), 
            "Detected charset " + actualCharset + " should support expected charset " + expectedCharset);
        
        // Verify the written bytes match the expected encoding
        assertArrayEquals(xml.getBytes(expectedEncoding), xmlBytes, 
            "Written bytes should match expected encoding");
    }

    /**
     * Tests XML writer with specified encoding using default encoding fallback.
     * 
     * @param text Text content to write
     * @param encoding Encoding specified in XML declaration (nullable)
     * @param defaultEncoding Fallback encoding if none specified (nullable)
     */
    private static void verifyXmlWriterWithEncoding(final String text, 
                                                   final String encoding, 
                                                   final String defaultEncoding) throws IOException {
        final String xml = createXmlWithDeclaration(text, encoding);
        final String resolvedEncoding = resolveEncoding(encoding, defaultEncoding);
        verifyXmlContentAndEncoding(xml, resolvedEncoding, defaultEncoding);
    }

    /**
     * Tests XML writer with specified encoding (uses UTF-8 as default fallback).
     */
    private static void verifyXmlWriterWithEncoding(final String text, 
                                                   final String encoding) throws IOException {
        verifyXmlWriterWithEncoding(text, encoding, null);
    }

    /**
     * Creates XML content with proper declaration.
     * 
     * @param text Text content to wrap in XML
     * @param encoding Encoding for declaration (nullable)
     * @return Full XML content with declaration
     */
    private static String createXmlWithDeclaration(final String text, final String encoding) {
        final String xmlDeclaration = encoding != null ? 
            String.format(XML_DECLARATION_WITH_ENCODING, encoding) : 
            XML_DECLARATION_NO_ENCODING;
            
        return xmlDeclaration + "\n<text>" + text + "</text>";
    }

    /**
     * Resolves the effective encoding handling null cases.
     */
    private static String resolveEncoding(String specifiedEncoding, String defaultEncoding) {
        if (specifiedEncoding != null) {
            return specifiedEncoding;
        }
        return defaultEncoding != null ? defaultEncoding : StandardCharsets.UTF_8.name();
    }

    // Test Cases ----------------------------------------------------------------------

    /**
     * Tests behavior when no explicit encoding is specified (uses default encodings).
     */
    @Test
    void testDefaultEncoding() throws IOException {
        verifyXmlWriterWithEncoding(TEXT_UNICODE, null, null);
        verifyXmlWriterWithEncoding(TEXT_UNICODE, null, StandardCharsets.UTF_8.name());
        verifyXmlWriterWithEncoding(TEXT_UNICODE, null, StandardCharsets.UTF_16.name());
        verifyXmlWriterWithEncoding(TEXT_UNICODE, null, StandardCharsets.UTF_16BE.name());
        verifyXmlWriterWithEncoding(TEXT_UNICODE, null, StandardCharsets.ISO_8859_1.name());
    }

    /** Tests EBCDIC encoding support. */
    @Test
    void testEbcdicEncoding() throws IOException {
        verifyXmlWriterWithEncoding("simple text in EBCDIC", "CP1047");
    }

    /** Tests writing empty content and small writes. */
    @Test
    void testEmptyContentAndPartialWrites() throws IOException {
        // Test with constructor
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                XmlStreamWriter writer = new XmlStreamWriter(out)) {
            writer.flush();
            writer.write("");
            writer.flush();
            writer.write(".");
            writer.flush();
        }
        
        // Test with builder
        try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                XmlStreamWriter writer = XmlStreamWriter.builder().setOutputStream(out).get()) {
            writer.flush();
            writer.write("");
            writer.flush();
            writer.write(".");
            writer.flush();
        }
    }

    /** Tests EUC-JP encoding (Japanese). */
    @Test
    void testEucJpEncoding() throws IOException {
        verifyXmlWriterWithEncoding(TEXT_EUC_JP, "EUC-JP");
    }

    /** Tests ISO-8859-15 encoding (Latin-15 with Euro symbol). */
    @Test
    void testLatin15Encoding() throws IOException {
        verifyXmlWriterWithEncoding(TEXT_LATIN15, "ISO-8859-15");
    }

    /** Tests ISO-8859-1 encoding (Latin-1). */
    @Test
    void testLatin1Encoding() throws IOException {
        verifyXmlWriterWithEncoding(TEXT_LATIN1, StandardCharsets.ISO_8859_1.name());
    }

    /** Tests ISO-8859-7 encoding (Latin/Greek). */
    @Test
    void testLatin7Encoding() throws IOException {
        verifyXmlWriterWithEncoding(TEXT_LATIN7, "ISO-8859-7");
    }

    /**
     * Tests encoding detection with Turkish locale (verifies case conversion stability).
     * See: https://issues.apache.org/jira/browse/IO-557
     */
    @Test
    @DefaultLocale(language = "tr")
    void testLowerCaseEncodingWithTurkishLocale() throws IOException {
        verifyXmlWriterWithEncoding(TEXT_UNICODE, "utf-8");
        verifyXmlWriterWithEncoding(TEXT_LATIN1, "iso-8859-1");
        verifyXmlWriterWithEncoding(TEXT_LATIN7, "iso-8859-7");
    }

    /** Tests content without XML declaration (should default to UTF-8). */
    @Test
    void testContentWithoutXmlDeclaration() throws IOException {
        verifyXmlContentAndEncoding(
            "<text>text with no XML header</text>", 
            StandardCharsets.UTF_8.name(), 
            null
        );
    }

    /** Tests UTF-16 Big Endian encoding. */
    @Test
    void testUtf16BigEndianEncoding() throws IOException {
        verifyXmlWriterWithEncoding(TEXT_UNICODE, StandardCharsets.UTF_16BE.name());
    }

    /** Tests UTF-16 encoding (with byte order mark). */
    @Test
    void testUtf16Encoding() throws IOException {
        verifyXmlWriterWithEncoding(TEXT_UNICODE, StandardCharsets.UTF_16.name());
    }

    /** Tests UTF-16 Little Endian encoding. */
    @Test
    void testUtf16LittleEndianEncoding() throws IOException {
        verifyXmlWriterWithEncoding(TEXT_UNICODE, StandardCharsets.UTF_16LE.name());
    }

    /** Tests standard UTF-8 encoding. */
    @Test
    void testUtf8Encoding() throws IOException {
        verifyXmlWriterWithEncoding(TEXT_UNICODE, StandardCharsets.UTF_8.name());
    }
}