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
 * Tests {@link XmlStreamWriter} to ensure it correctly handles XML encoding detection and writing.
 * 
 * The XmlStreamWriter automatically detects the appropriate encoding for XML content
 * based on XML declaration headers and default encoding settings.
 */
class XmlStreamWriterTest {

    // Test data with characters specific to different encodings
    private static final String FRENCH_TEXT = "eacute: \u00E9";           // French - requires Latin-1
    private static final String GREEK_TEXT = "alpha: \u03B1";            // Greek - requires Latin-7
    private static final String EURO_TEXT = "euro: \u20AC";              // Euro symbol - requires Latin-15
    private static final String JAPANESE_TEXT = "hiragana A: \u3042";     // Japanese - requires EUC-JP
    
    // Combined Unicode text that requires UTF encoding to represent all characters
    private static final String UNICODE_TEXT = FRENCH_TEXT + ", " + GREEK_TEXT
            + ", " + EURO_TEXT + ", " + JAPANESE_TEXT;

    /**
     * Verifies that XmlStreamWriter correctly writes XML content with the expected encoding.
     * 
     * @param xmlContent The complete XML document to write
     * @param expectedEncodingName The encoding that should be used for writing
     * @param defaultEncodingName The default encoding to configure the writer with (can be null)
     */
    @SuppressWarnings("resource")
    private static void verifyXmlWrittenWithCorrectEncoding(final String xmlContent, 
            final String expectedEncodingName, final String defaultEncodingName) throws IOException {
        
        // Arrange: Set up output stream and writer
        final ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        final XmlStreamWriter xmlWriter;
        
        // Act: Write the XML content
        try (XmlStreamWriter writer = XmlStreamWriter.builder()
                .setOutputStream(outputStream)
                .setCharset(defaultEncodingName)
                .get()) {
            xmlWriter = writer;
            writer.write(xmlContent);
        }
        
        // Assert: Verify the encoding was detected correctly and content matches
        final byte[] writtenBytes = outputStream.toByteArray();
        final Charset expectedCharset = Charset.forName(expectedEncodingName);
        final Charset actualCharset = Charset.forName(xmlWriter.getEncoding());
        
        assertEquals(expectedCharset, actualCharset, 
            "Writer should use the expected encoding");
        assertTrue(actualCharset.contains(expectedCharset), 
            "Actual charset should contain the expected charset: " + actualCharset.name());
        assertArrayEquals(xmlContent.getBytes(expectedEncodingName), writtenBytes,
            "Written bytes should match the expected encoding");
    }

    /**
     * Tests XmlStreamWriter with specific text and encoding, using UTF-8 as default.
     */
    private static void testXmlWriterWithEncoding(final String textContent, final String encoding)
            throws IOException {
        testXmlWriterWithEncoding(textContent, encoding, null);
    }

    /**
     * Tests XmlStreamWriter with specific text, encoding, and default encoding.
     * 
     * @param textContent The text content to include in the XML
     * @param declaredEncoding The encoding declared in XML header (null for no declaration)
     * @param defaultEncoding The default encoding for the writer (null for UTF-8 default)
     */
    private static void testXmlWriterWithEncoding(final String textContent, 
            final String declaredEncoding, final String defaultEncoding) throws IOException {
        
        // Create complete XML document with or without encoding declaration
        final String xmlDocument = createXmlDocument(textContent, declaredEncoding);
        
        // Determine what encoding should actually be used
        String expectedEncoding = declaredEncoding;
        if (expectedEncoding == null) {
            expectedEncoding = defaultEncoding == null ? StandardCharsets.UTF_8.name() : defaultEncoding;
        }
        
        verifyXmlWrittenWithCorrectEncoding(xmlDocument, expectedEncoding, defaultEncoding);
    }

    /**
     * Creates a complete XML document with optional encoding declaration.
     * 
     * @param textContent The text content for the XML body
     * @param encoding The encoding to declare in XML header (null for no encoding declaration)
     * @return Complete XML document string
     */
    private static String createXmlDocument(final String textContent, final String encoding) {
        String xmlDeclaration = "<?xml version=\"1.0\"?>";
        if (encoding != null) {
            xmlDeclaration = "<?xml version=\"1.0\" encoding=\"" + encoding + "\"?>";
        }
        return xmlDeclaration + "\n<text>" + textContent + "</text>";
    }

    @Test
    void shouldUseCorrectDefaultEncodingWhenNoEncodingDeclared() throws IOException {
        // Test with no default encoding specified (should use UTF-8)
        testXmlWriterWithEncoding(UNICODE_TEXT, null, null);
        
        // Test with various default encodings
        testXmlWriterWithEncoding(UNICODE_TEXT, null, StandardCharsets.UTF_8.name());
        testXmlWriterWithEncoding(UNICODE_TEXT, null, StandardCharsets.UTF_16.name());
        testXmlWriterWithEncoding(UNICODE_TEXT, null, StandardCharsets.UTF_16BE.name());
        testXmlWriterWithEncoding(UNICODE_TEXT, null, StandardCharsets.ISO_8859_1.name());
    }

    @Test
    void shouldHandleEbcdicEncoding() throws IOException {
        testXmlWriterWithEncoding("simple text in EBCDIC", "CP1047");
    }

    @Test
    void shouldHandleEmptyAndMinimalContent() throws IOException {
        // Test with deprecated constructor
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                XmlStreamWriter writer = new XmlStreamWriter(outputStream)) {
            writer.flush();
            writer.write("");
            writer.flush();
            writer.write(".");
            writer.flush();
        }
        
        // Test with builder pattern
        try (ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
                XmlStreamWriter writer = XmlStreamWriter.builder()
                    .setOutputStream(outputStream)
                    .get()) {
            writer.flush();
            writer.write("");
            writer.flush();
            writer.write(".");
            writer.flush();
        }
    }

    @Test
    void shouldHandleJapaneseEucJpEncoding() throws IOException {
        testXmlWriterWithEncoding(JAPANESE_TEXT, "EUC-JP");
    }

    @Test
    void shouldHandleLatin15EncodingForEuroSymbol() throws IOException {
        testXmlWriterWithEncoding(EURO_TEXT, "ISO-8859-15");
    }

    @Test
    void shouldHandleLatin1EncodingForFrenchText() throws IOException {
        testXmlWriterWithEncoding(FRENCH_TEXT, StandardCharsets.ISO_8859_1.name());
    }

    @Test
    void shouldHandleLatin7EncodingForGreekText() throws IOException {
        testXmlWriterWithEncoding(GREEK_TEXT, "ISO-8859-7");
    }

    /**
     * Tests encoding handling with Turkish locale.
     * Turkish language has specific rules for converting dotted and dotless 'i' characters,
     * which can affect case-insensitive encoding name matching.
     */
    @Test
    @DefaultLocale(language = "tr")
    void shouldHandleLowerCaseEncodingNamesInTurkishLocale() throws IOException {
        testXmlWriterWithEncoding(UNICODE_TEXT, "utf-8");
        testXmlWriterWithEncoding(FRENCH_TEXT, "iso-8859-1");
        testXmlWriterWithEncoding(GREEK_TEXT, "iso-8859-7");
    }

    @Test
    void shouldHandleXmlWithoutHeader() throws IOException {
        verifyXmlWrittenWithCorrectEncoding(
            "<text>text with no XML header</text>", 
            StandardCharsets.UTF_8.name(), 
            null);
    }

    @Test
    void shouldHandleUtf16BigEndianEncoding() throws IOException {
        testXmlWriterWithEncoding(UNICODE_TEXT, StandardCharsets.UTF_16BE.name());
    }

    @Test
    void shouldHandleUtf16Encoding() throws IOException {
        testXmlWriterWithEncoding(UNICODE_TEXT, StandardCharsets.UTF_16.name());
    }

    @Test
    void shouldHandleUtf16LittleEndianEncoding() throws IOException {
        testXmlWriterWithEncoding(UNICODE_TEXT, StandardCharsets.UTF_16LE.name());
    }

    @Test
    void shouldHandleUtf8Encoding() throws IOException {
        testXmlWriterWithEncoding(UNICODE_TEXT, StandardCharsets.UTF_8.name());
    }
}