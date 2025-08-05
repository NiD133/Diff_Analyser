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
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junitpioneer.jupiter.DefaultLocale;

/**
 * Tests for {@link XmlStreamWriter}, focusing on its ability to automatically
 * detect and apply the correct character encoding from the XML declaration.
 */
class XmlStreamWriterTest {

    // --- Test Data Constants ---

    /** French text with characters from ISO-8859-1. */
    private static final String TEXT_LATIN1 = "eacute: \u00E9";

    /** Greek text with characters from ISO-8859-7. */
    private static final String TEXT_LATIN7 = "alpha: \u03B1";

    /** Text with the Euro symbol, requiring ISO-8859-15 or Unicode. */
    private static final String TEXT_LATIN15 = "euro: \u20AC";

    /** Japanese text with characters from EUC-JP. */
    private static final String TEXT_EUC_JP = "hiragana A: \u3042";

    /** A composite string with characters from various character sets. */
    private static final String TEXT_UNICODE = TEXT_LATIN1 + ", " + TEXT_LATIN7 + ", " + TEXT_LATIN15 + ", " + TEXT_EUC_JP;

    // --- Charset Name Constants ---
    private static final String CS_EUC_JP = "EUC-JP";
    private static final String CS_ISO_8859_7 = "ISO-8859-7";
    private static final String CS_ISO_8859_15 = "ISO-8859-15";
    private static final String CS_CP1047 = "CP1047"; // EBCDIC

    // --- Test Cases ---

    @DisplayName("Writer should use encoding specified in the XML declaration")
    @ParameterizedTest(name = "Encoding: {1}")
    @MethodSource("provideDeclaredEncodings")
    void write_whenXmlDeclaresEncoding_usesDeclaredEncoding(final String text, final String encodingName) throws IOException {
        final String xml = createXmlWithHeader(text, encodingName);
        // No default encoding is provided, so the writer must detect it from the stream.
        assertXmlWrite(xml, encodingName, null);
    }

    @DisplayName("Writer should use the default encoding when it is not in the XML declaration")
    @ParameterizedTest(name = "Default: {0}, Expected: {1}")
    @MethodSource("provideDefaultEncodings")
    void write_whenXmlDeclLacksEncoding_usesDefaultEncoding(final String defaultEncodingName, final String expectedEncodingName) throws IOException {
        // XML header without an 'encoding' attribute.
        final String xml = createXmlWithHeader(TEXT_UNICODE, null);
        assertXmlWrite(xml, expectedEncodingName, defaultEncodingName);
    }

    @Test
    @DisplayName("Writer should default to UTF-8 for content without an XML header")
    void write_whenXmlHeaderIsMissing_usesDefaultUTF8() throws IOException {
        final String xml = "<text>text with no XML header</text>";
        // When no default encoding is provided and none can be detected, XmlStreamWriter defaults to UTF-8.
        assertXmlWrite(xml, StandardCharsets.UTF_8.name(), null);
    }

    @DisplayName("Writer should correctly detect encoding in Turkish locale (IO-557)")
    @ParameterizedTest(name = "Encoding: {1}")
    @MethodSource("provideTurkishLocaleEncodings")
    @DefaultLocale(language = "tr")
    void write_withTurkishLocale_detectsEncodingCorrectly_IO_557(final String text, final String encodingName) throws IOException {
        final String xml = createXmlWithHeader(text, encodingName);
        assertXmlWrite(xml, encodingName, null);
    }

    @Test
    @DisplayName("Write operations should not throw when writer is created with deprecated constructor")
    void write_withEmptyAndSingleChar_doesNotThrowForConstructor() {
        assertDoesNotThrow(() -> {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                 // Using deprecated constructor for test coverage.
                 @SuppressWarnings("deprecation")
                 XmlStreamWriter writer = new XmlStreamWriter(out)) {
                writer.flush();
                writer.write("");
                writer.flush();
                writer.write(".");
                writer.flush();
            }
        }, "Writing empty or minimal content should not throw an exception.");
    }

    @Test
    @DisplayName("Write operations should not throw when writer is created with builder")
    void write_withEmptyAndSingleChar_doesNotThrowForBuilder() {
        assertDoesNotThrow(() -> {
            try (ByteArrayOutputStream out = new ByteArrayOutputStream();
                 XmlStreamWriter writer = XmlStreamWriter.builder().setOutputStream(out).get()) {
                writer.flush();
                writer.write("");
                writer.flush();
                writer.write(".");
                writer.flush();
            }
        }, "Writing empty or minimal content should not throw an exception.");
    }

    // --- Helper Methods ---

    /**
     * Asserts that writing an XML string with an {@link XmlStreamWriter} results in the
     * expected encoding and byte sequence.
     *
     * @param xmlContent The full XML string to write.
     * @param expectedEncodingName The encoding that the writer is expected to detect and use.
     * @param defaultEncodingName The default encoding to configure the writer with, can be {@code null}.
     * @throws IOException If an I/O error occurs.
     */
    private void assertXmlWrite(final String xmlContent, final String expectedEncodingName, final String defaultEncodingName) throws IOException {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();
        final String detectedEncoding;

        final Charset defaultCharset = defaultEncodingName != null ? Charset.forName(defaultEncodingName) : null;

        try (XmlStreamWriter writer = XmlStreamWriter.builder()
                .setOutputStream(out)
                .setCharset(defaultCharset)
                .get()) {
            writer.write(xmlContent);
            // Retrieve the detected encoding before the writer is closed.
            detectedEncoding = writer.getEncoding();
        }

        // 1. Verify the detected encoding is correct.
        assertEquals(expectedEncodingName, detectedEncoding, "Detected encoding should match the expected encoding.");

        // 2. Verify the written bytes are correct for the expected encoding.
        final byte[] expectedBytes = xmlContent.getBytes(expectedEncodingName);
        assertArrayEquals(expectedBytes, out.toByteArray(), "The byte output should match the input string encoded with the expected charset.");
    }

    /**
     * Creates an XML document string with a standard header.
     *
     * @param text The text content for the {@code <text>} element.
     * @param encodingName The encoding to specify in the XML declaration. If {@code null}, the encoding attribute is omitted.
     * @return A string containing the XML document.
     */
    private static String createXmlWithHeader(final String text, final String encodingName) {
        final String xmlDecl = encodingName != null
            ? "<?xml version=\"1.0\" encoding=\"" + encodingName + "\"?>"
            : "<?xml version=\"1.0\"?>";
        return xmlDecl + "\n<text>" + text + "</text>";
    }

    // --- Data Providers for Parameterized Tests ---

    private static Stream<Arguments> provideDeclaredEncodings() {
        return Stream.of(
            Arguments.of(TEXT_UNICODE, StandardCharsets.UTF_8.name()),
            Arguments.of(TEXT_UNICODE, StandardCharsets.UTF_16.name()),
            Arguments.of(TEXT_UNICODE, StandardCharsets.UTF_16BE.name()),
            Arguments.of(TEXT_UNICODE, StandardCharsets.UTF_16LE.name()),
            Arguments.of(TEXT_LATIN1, StandardCharsets.ISO_8859_1.name()),
            Arguments.of(TEXT_LATIN7, CS_ISO_8859_7),
            Arguments.of(TEXT_LATIN15, CS_ISO_8859_15),
            Arguments.of(TEXT_EUC_JP, CS_EUC_JP),
            Arguments.of("simple text in EBCDIC", CS_CP1047)
        );
    }

    private static Stream<Arguments> provideDefaultEncodings() {
        return Stream.of(
            // If default is null, writer should fall back to UTF-8.
            Arguments.of(null, StandardCharsets.UTF_8.name()),
            Arguments.of(StandardCharsets.UTF_8.name(), StandardCharsets.UTF_8.name()),
            Arguments.of(StandardCharsets.UTF_16.name(), StandardCharsets.UTF_16.name()),
            Arguments.of(StandardCharsets.UTF_16BE.name(), StandardCharsets.UTF_16BE.name()),
            Arguments.of(StandardCharsets.ISO_8859_1.name(), StandardCharsets.ISO_8859_1.name())
        );
    }

    private static Stream<Arguments> provideTurkishLocaleEncodings() {
        return Stream.of(
            Arguments.of(TEXT_UNICODE, StandardCharsets.UTF_8.name()),
            Arguments.of(TEXT_LATIN1, StandardCharsets.ISO_8859_1.name()),
            Arguments.of(TEXT_LATIN7, CS_ISO_8859_7)
        );
    }
}