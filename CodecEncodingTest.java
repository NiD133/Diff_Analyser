/*
 * Licensed to the Apache Software Foundation (ASF) under one
 * or more contributor license agreements.  See the NOTICE file
 * distributed with this work for additional information
 * regarding copyright ownership.  The ASF licenses this file
 * to you under the Apache License, Version 2.0 (the
 * "License"); you may not use this file except in compliance
 * with the License.  You may obtain a copy of the License at
 *
 *   https://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing,
 * software distributed under the License is distributed on an
 * "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
 * KIND, either express or implied.  See the License for the
 * specific language governing permissions and limitations
 * under the License.
 */
package org.apache.commons.compress.harmony.pack200;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for the {@link CodecEncoding} class, focusing on codec retrieval and specifier generation.
 */
class CodecEncodingTest {

    /**
     * Provides a stream of arguments for testing the retrieval of arbitrary codecs. Each argument consists of:
     * <ul>
     *     <li>A string representation of the expected codec.</li>
     *     <li>A byte array representing the encoding data.</li>
     * </ul>
     */
    static Stream<Arguments> arbitraryCodecData() {
        return Stream.of(
            Arguments.of("(1,256)", new byte[] { 0x00, (byte) 0xFF }),
            Arguments.of("(5,128,2,1)", new byte[] { 0x25, (byte) 0x7F }),
            Arguments.of("(2,128,1,1)", new byte[] { 0x0B, (byte) 0x7F })
        );
    }

    /**
     * Provides a stream of arguments for testing the retrieval of canonical codecs based on their index. Each argument consists of:
     * <ul>
     *     <li>The canonical encoding index.</li>
     *     <li>A string representation of the expected codec.</li>
     * </ul>
     * These canonical encodings are as defined by the Pack200 specification.
     */
    static Stream<Arguments> canonicalEncodingData() {
        return Stream.of(
            Arguments.of(1, "(1,256)"), Arguments.of(2, "(1,256,1)"), Arguments.of(3, "(1,256,0,1)"),
            Arguments.of(4, "(1,256,1,1)"), Arguments.of(5, "(2,256)"), Arguments.of(6, "(2,256,1)"),
            Arguments.of(7, "(2,256,0,1)"), Arguments.of(8, "(2,256,1,1)"), Arguments.of(9, "(3,256)"),
            Arguments.of(10, "(3,256,1)"), Arguments.of(11, "(3,256,0,1)"), Arguments.of(12, "(3,256,1,1)"),
            Arguments.of(13, "(4,256)"), Arguments.of(14, "(4,256,1)"), Arguments.of(15, "(4,256,0,1)"),
            Arguments.of(16, "(4,256,1,1)"), Arguments.of(17, "(5,4)"), Arguments.of(18, "(5,4,1)"),
            Arguments.of(19, "(5,4,2)"), Arguments.of(20, "(5,16)"), Arguments.of(21, "(5,16,1)"),
            Arguments.of(22, "(5,16,2)"), Arguments.of(23, "(5,32)"), Arguments.of(24, "(5,32,1)"),
            Arguments.of(25, "(5,32,2)"), Arguments.of(26, "(5,64)"), Arguments.of(27, "(5,64,1)"),
            Arguments.of(28, "(5,64,2)"), Arguments.of(29, "(5,128)"), Arguments.of(30, "(5,128,1)"),
            Arguments.of(31, "(5,128,2)"), Arguments.of(32, "(5,4,0,1)"), Arguments.of(33, "(5,4,1,1)"),
            Arguments.of(34, "(5,4,2,1)"), Arguments.of(35, "(5,16,0,1)"), Arguments.of(36, "(5,16,1,1)"),
            Arguments.of(37, "(5,16,2,1)"), Arguments.of(38, "(5,32,0,1)"), Arguments.of(39, "(5,32,1,1)"),
            Arguments.of(40, "(5,32,2,1)"), Arguments.of(41, "(5,64,0,1)"), Arguments.of(42, "(5,64,1,1)"),
            Arguments.of(43, "(5,64,2,1)"), Arguments.of(44, "(5,128,0,1)"), Arguments.of(45, "(5,128,1,1)"),
            Arguments.of(46, "(5,128,2,1)"), Arguments.of(47, "(2,192)"), Arguments.of(48, "(2,224)"),
            Arguments.of(49, "(2,240)"), Arguments.of(50, "(2,248)"), Arguments.of(51, "(2,252)"),
            Arguments.of(52, "(2,8,0,1)"), Arguments.of(53, "(2,8,1,1)"), Arguments.of(54, "(2,16,0,1)"),
            Arguments.of(55, "(2,16,1,1)"), Arguments.of(56, "(2,32,0,1)"), Arguments.of(57, "(2,32,1,1)"),
            Arguments.of(58, "(2,64,0,1)"), Arguments.of(59, "(2,64,1,1)"), Arguments.of(60, "(2,128,0,1)"),
            Arguments.of(61, "(2,128,1,1)"), Arguments.of(62, "(2,192,0,1)"), Arguments.of(63, "(2,192,1,1)"),
            Arguments.of(64, "(2,224,0,1)"), Arguments.of(65, "(2,224,1,1)"), Arguments.of(66, "(2,240,0,1)"),
            Arguments.of(67, "(2,240,1,1)"), Arguments.of(68, "(2,248,0,1)"), Arguments.of(69, "(2,248,1,1)"),
            Arguments.of(70, "(3,192)"), Arguments.of(71, "(3,224)"), Arguments.of(72, "(3,240)"),
            Arguments.of(73, "(3,248)"), Arguments.of(74, "(3,252)"), Arguments.of(75, "(3,8,0,1)"),
            Arguments.of(76, "(3,8,1,1)"), Arguments.of(77, "(3,16,0,1)"), Arguments.of(78, "(3,16,1,1)"),
            Arguments.of(79, "(3,32,0,1)"), Arguments.of(80, "(3,32,1,1)"), Arguments.of(81, "(3,64,0,1)"),
            Arguments.of(82, "(3,64,1,1)"), Arguments.of(83, "(3,128,0,1)"), Arguments.of(84, "(3,128,1,1)"),
            Arguments.of(85, "(3,192,0,1)"), Arguments.of(86, "(3,192,1,1)"), Arguments.of(87, "(3,224,0,1)"),
            Arguments.of(88, "(3,224,1,1)"), Arguments.of(89, "(3,240,0,1)"), Arguments.of(90, "(3,240,1,1)"),
            Arguments.of(91, "(3,248,0,1)"), Arguments.of(92, "(3,248,1,1)"), Arguments.of(93, "(4,192)"),
            Arguments.of(94, "(4,224)"), Arguments.of(95, "(4,240)"), Arguments.of(96, "(4,248)"),
            Arguments.of(97, "(4,252)"), Arguments.of(98, "(4,8,0,1)"), Arguments.of(99, "(4,8,1,1)"),
            Arguments.of(100, "(4,16,0,1)"), Arguments.of(101, "(4,16,1,1)"), Arguments.of(102, "(4,32,0,1)"),
            Arguments.of(103, "(4,32,1,1)"), Arguments.of(104, "(4,64,0,1)"), Arguments.of(105, "(4,64,1,1)"),
            Arguments.of(106, "(4,128,0,1)"), Arguments.of(107, "(4,128,1,1)"), Arguments.of(108, "(4,192,0,1)"),
            Arguments.of(109, "(4,192,1,1)"), Arguments.of(110, "(4,224,0,1)"), Arguments.of(111, "(4,224,1,1)"),
            Arguments.of(112, "(4,240,0,1)"), Arguments.of(113, "(4,240,1,1)"), Arguments.of(114, "(4,248,0,1)"),
            Arguments.of(115, "(4,248,1,1)")
        );
    }

    /**
     * Provides a stream of arguments for testing the retrieval of the specifier for a canonical codec.
     * The specifier is simply the index of the codec in the canonical codec array.
     */
    static Stream<Arguments> canonicalCodecSpecifierData() {
        return IntStream.range(1, 115).mapToObj(Arguments::of);
    }

    /**
     * Provides a stream of arbitrary {@link BHSDCodec} instances for testing.
     */
    static Stream<Arguments> arbitraryBHSDCodecData() {
        return Stream.of(
            Arguments.of(new BHSDCodec(2, 125, 0, 1)),
            Arguments.of(new BHSDCodec(3, 125, 2, 1)),
            Arguments.of(new BHSDCodec(4, 125)),
            Arguments.of(new BHSDCodec(5, 125, 2, 0)),
            Arguments.of(new BHSDCodec(3, 5, 2, 1))
        );
    }

    /**
     * Tests the retrieval of an arbitrary codec given its encoding data.
     *
     * @param expectedCodecString The string representation of the expected codec.
     * @param encodingBytes       The byte array containing the encoding data.
     * @throws IOException        If an I/O error occurs.
     * @throws Pack200Exception   If a Pack200 exception occurs.
     */
    @ParameterizedTest
    @MethodSource("arbitraryCodecData")
    void testArbitraryCodecRetrieval(final String expectedCodecString, final byte[] encodingBytes) throws IOException, Pack200Exception {
        try (final ByteArrayInputStream inputStream = new ByteArrayInputStream(encodingBytes)) {
            final Codec codec = CodecEncoding.getCodec(116, inputStream, null);
            assertEquals(expectedCodecString, codec.toString(), "The retrieved codec should match the expected codec.");
        }
    }

    /**
     * Tests the retrieval of canonical codecs based on their index.
     *
     * @param codecIndex      The index of the canonical codec.
     * @param expectedCodecString The string representation of the expected codec.
     * @throws IOException      If an I/O error occurs.
     * @throws Pack200Exception If a Pack200 exception occurs.
     */
    @ParameterizedTest
    @MethodSource("canonicalEncodingData")
    void testCanonicalCodecRetrieval(final int codecIndex, final String expectedCodecString) throws IOException, Pack200Exception {
        final Codec codec = CodecEncoding.getCodec(codecIndex, null, null);
        assertEquals(expectedCodecString, codec.toString(), "The retrieved codec should match the expected canonical codec.");
    }

    /**
     * Tests the retrieval of the specifier for canonical codecs based on their index. The specifier should equal the index.
     *
     * @param codecIndex The index of the canonical codec.
     * @throws IOException      If an I/O error occurs.
     * @throws Pack200Exception If a Pack200 exception occurs.
     */
    @ParameterizedTest
    @MethodSource("canonicalCodecSpecifierData")
    void testCanonicalCodecSpecifierRetrieval(final int codecIndex) throws Pack200Exception, IOException {
        final Codec codec = CodecEncoding.getCodec(codecIndex, null, null);
        final int[] specifiers = CodecEncoding.getSpecifier(codec, null);
        assertEquals(codecIndex, specifiers[0], "The specifier should equal the codec index.");
    }

    /**
     * Tests the retrieval of the default codec.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws Pack200Exception If a Pack200 exception occurs.
     */
    @Test
    void testDefaultCodecRetrieval() throws Pack200Exception, IOException {
        final Codec defaultCodec = new BHSDCodec(2, 16, 0, 0);
        final Codec retrievedCodec = CodecEncoding.getCodec(0, null, defaultCodec);
        assertEquals(defaultCodec, retrievedCodec, "The retrieved codec should be the default codec.");
    }

    /**
     * Tests the retrieval of the specifier for a {@link PopulationCodec}. This also implicitly tests the round-trip
     * retrieval of a population codec from its specifier data.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws Pack200Exception If a Pack200 exception occurs.
     */
    @Test
    void testPopulationCodecSpecifierRetrieval() throws IOException, Pack200Exception {
        final Codec byte1 = Codec.BYTE1;
        final Codec char3 = Codec.CHAR3;
        final Codec unsigned5 = Codec.UNSIGNED5;
        final PopulationCodec populationCodec = new PopulationCodec(byte1, char3, unsigned5);

        final int[] specifiers = CodecEncoding.getSpecifier(populationCodec, null);
        assertTrue(specifiers[0] > 140 && specifiers[0] < 189, "The first specifier byte should be in the range (140, 189).");

        final byte[] encodingBytes = new byte[specifiers.length - 1];
        for (int i = 0; i < encodingBytes.length; i++) {
            encodingBytes[i] = (byte) specifiers[i + 1];
        }

        try (final InputStream inputStream = new ByteArrayInputStream(encodingBytes)) {
            final PopulationCodec retrievedCodec = (PopulationCodec) CodecEncoding.getCodec(specifiers[0], inputStream, null);

            assertEquals(populationCodec.getFavouredCodec(), retrievedCodec.getFavouredCodec(), "Favoured Codec should match.");
            assertEquals(populationCodec.getTokenCodec(), retrievedCodec.getTokenCodec(), "Token Codec should match.");
            assertEquals(populationCodec.getUnfavouredCodec(), retrievedCodec.getUnfavouredCodec(), "Unfavoured Codec should match.");
        }
    }

    /**
     * Tests the retrieval of the specifier for a {@link RunCodec}. This also implicitly tests the round-trip
     * retrieval of a RunCodec from its specifier data.  Also covers cases where a default codec is in use,
     * and nested RunCodecs.
     *
     * @throws IOException      If an I/O error occurs.
     * @throws Pack200Exception If a Pack200 exception occurs.
     */
    @Test
    void testRunCodecSpecifierRetrieval() throws Pack200Exception, IOException {
        Codec delta5 = Codec.DELTA5;
        Codec byte1 = Codec.BYTE1;

        // Basic RunCodec
        RunCodec runCodec = new RunCodec(25, delta5, byte1);
        int[] specifiers = CodecEncoding.getSpecifier(runCodec, null);
        assertTrue(specifiers[0] > 116 && specifiers[0] < 141, "The first specifier byte should be in the range (116, 141).");

        byte[] encodingBytes = new byte[specifiers.length - 1];
        for (int i = 0; i < encodingBytes.length; i++) {
            encodingBytes[i] = (byte) specifiers[i + 1];
        }
        try (InputStream in = new ByteArrayInputStream(encodingBytes)) {
            RunCodec runCodec2 = (RunCodec) CodecEncoding.getCodec(specifiers[0], in, null);
            assertEquals(runCodec.getK(), runCodec2.getK(), "Run length K should match.");
            assertEquals(runCodec.getACodec(), runCodec2.getACodec(), "ACodec should match.");
            assertEquals(runCodec.getBCodec(), runCodec2.getBCodec(), "BCodec should match.");
        }

        // One codec is the same as the default
        runCodec = new RunCodec(4096, delta5, byte1);
        specifiers = CodecEncoding.getSpecifier(runCodec, delta5);
        assertTrue(specifiers[0] > 116 && specifiers[0] < 141, "The first specifier byte should be in the range (116, 141).");
        encodingBytes = new byte[specifiers.length - 1];
        for (int i = 0; i < encodingBytes.length; i++) {
            encodingBytes[i] = (byte) specifiers[i + 1];
        }
        try (InputStream in = new ByteArrayInputStream(encodingBytes)) {
            RunCodec runCodec2 = (RunCodec) CodecEncoding.getCodec(specifiers[0], in, delta5);
            assertEquals(runCodec.getK(), runCodec2.getK(), "Run length K should match.");
            assertEquals(runCodec.getACodec(), runCodec2.getACodec(), "ACodec should match.");
            assertEquals(runCodec.getBCodec(), runCodec2.getBCodec(), "BCodec should match.");
        }

        // Nested run codecs
        Codec signed5 = Codec.SIGNED5;
        Codec udelta5 = Codec.UDELTA5;
        runCodec = new RunCodec(64, signed5, new RunCodec(25, udelta5, delta5));
        specifiers = CodecEncoding.getSpecifier(runCodec, null);
        assertTrue(specifiers[0] > 116 && specifiers[0] < 141, "The first specifier byte should be in the range (116, 141).");
        encodingBytes = new byte[specifiers.length - 1];
        for (int i = 0; i < encodingBytes.length; i++) {
            encodingBytes[i] = (byte) specifiers[i + 1];
        }
        try (InputStream in = new ByteArrayInputStream(encodingBytes)) {
            RunCodec runCodec2 = (RunCodec) CodecEncoding.getCodec(specifiers[0], in, null);
            assertEquals(runCodec.getK(), runCodec2.getK(), "Run length K should match.");
            assertEquals(runCodec.getACodec(), runCodec2.getACodec(), "ACodec should match.");
            RunCodec bCodec = (RunCodec) runCodec.getBCodec();
            RunCodec bCodec2 = (RunCodec) runCodec2.getBCodec();
            assertEquals(bCodec.getK(), bCodec2.getK(), "Nested BCodec run length K should match.");
            assertEquals(bCodec.getACodec(), bCodec2.getACodec(), "Nested BCodec ACodec should match.");
            assertEquals(bCodec.getBCodec(), bCodec2.getBCodec(), "Nested BCodec BCodec should match.");
        }

        // Nested with one the same as the default
        runCodec = new RunCodec(64, signed5, new RunCodec(25, udelta5, delta5));
        specifiers = CodecEncoding.getSpecifier(runCodec, udelta5);
        assertTrue(specifiers[0] > 116 && specifiers[0] < 141, "The first specifier byte should be in the range (116, 141).");
        encodingBytes = new byte[specifiers.length - 1];
        for (int i = 0; i < encodingBytes.length; i++) {
            encodingBytes[i] = (byte) specifiers[i + 1];
        }
        try (InputStream in = new ByteArrayInputStream(encodingBytes)) {
            RunCodec runCodec2 = (RunCodec) CodecEncoding.getCodec(specifiers[0], in, udelta5);
            assertEquals(runCodec.getK(), runCodec2.getK(), "Run length K should match.");
            assertEquals(runCodec.getACodec(), runCodec2.getACodec(), "ACodec should match.");
            RunCodec bCodec = (RunCodec) runCodec.getBCodec();
            RunCodec bCodec2 = (RunCodec) runCodec2.getBCodec();
            assertEquals(bCodec.getK(), bCodec2.getK(), "Nested BCodec run length K should match.");
            assertEquals(bCodec.getACodec(), bCodec2.getACodec(), "Nested BCodec ACodec should match.");
            assertEquals(bCodec.getBCodec(), bCodec2.getBCodec(), "Nested BCodec BCodec should match.");
        }
    }

    /**
     * Tests the retrieval of the specifier for an arbitrary {@link BHSDCodec}. Also implicitly tests the round-trip
     * retrieval of a BHSDCodec from its specifier data.
     *
     * @param codec The BHSDCodec to test.
     * @throws IOException      If an I/O error occurs.
     * @throws Pack200Exception If a Pack200 exception occurs.
     */
    @ParameterizedTest
    @MethodSource("arbitraryBHSDCodecData")
    void testBHSDCodecSpecifierRetrieval(final Codec codec) throws IOException, Pack200Exception {
        final int[] specifiers = CodecEncoding.getSpecifier(codec, null);
        assertEquals(3, specifiers.length, "The specifier array should have a length of 3.");
        assertEquals(116, specifiers[0], "The first specifier byte should be 116.");

        final byte[] encodingBytes = { (byte) specifiers[1], (byte) specifiers[2] };
        try (final InputStream inputStream = new ByteArrayInputStream(encodingBytes)) {
            final Codec retrievedCodec = CodecEncoding.getCodec(116, inputStream, null);
            assertEquals(codec, retrievedCodec, "The retrieved codec should match the original codec.");
        }
    }
}