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
 * Tests for CodecEncoding.
 *
 * This suite focuses on:
 * - Canonical encodings (specifiers 1..115)
 * - Arbitrary BHSD encodings (specifier 116 + 2 bytes)
 * - RunCodec encodings (specifiers 117..140)
 * - PopulationCodec encodings (specifiers 141..188)
 * - Specifier round-trips (encode -> decode)
 */
class CodecEncodingTest {

    // Specifier ranges from the Pack200 specification.
    private static final int SPECIFIER_CANONICAL_MAX = 115; // 1..115
    private static final int SPECIFIER_ARBITRARY_BHSD = 116; // uses 2 extra bytes
    private static final int SPECIFIER_RUN_MIN = 117, SPECIFIER_RUN_MAX = 140; // RunCodec range
    private static final int SPECIFIER_POP_MIN = 141, SPECIFIER_POP_MAX = 188; // PopulationCodec range

    // Helpers

    private static byte[] specifierTailToBytes(final int[] specifiers) {
        final byte[] bytes = new byte[specifiers.length - 1];
        for (int i = 1; i < specifiers.length; i++) {
            bytes[i - 1] = (byte) specifiers[i];
        }
        return bytes;
    }

    private static Codec decodeFromSpecifiers(final int[] specifiers, final Codec defaultCodec) throws IOException, Pack200Exception {
        try (InputStream in = new ByteArrayInputStream(specifierTailToBytes(specifiers))) {
            return CodecEncoding.getCodec(specifiers[0], in, defaultCodec);
        }
    }

    private static void assertInclusiveRange(final int value, final int minInclusive, final int maxInclusive, final String what) {
        assertTrue(value >= minInclusive && value <= maxInclusive,
                () -> what + " specifier out of range: " + value + " not in [" + minInclusive + "," + maxInclusive + "]");
    }

    // Parameter sources

    static Stream<Arguments> arbitraryCodec() {
        return Stream.of(
                Arguments.of("(1,256)", new byte[] { 0x00, (byte) 0xFF }),
                Arguments.of("(5,128,2,1)", new byte[] { 0x25, (byte) 0x7F }),
                Arguments.of("(2,128,1,1)", new byte[] { 0x0B, (byte) 0x7F })
        );
    }

    // Canonical encodings as specified by the Pack200 spec
    static Stream<Arguments> canonicalEncodings() {
        return Stream.of(
                Arguments.of(1, "(1,256)"), Arguments.of(2, "(1,256,1)"), Arguments.of(3, "(1,256,0,1)"), Arguments.of(4, "(1,256,1,1)"),
                Arguments.of(5, "(2,256)"), Arguments.of(6, "(2,256,1)"), Arguments.of(7, "(2,256,0,1)"), Arguments.of(8, "(2,256,1,1)"),
                Arguments.of(9, "(3,256)"), Arguments.of(10, "(3,256,1)"), Arguments.of(11, "(3,256,0,1)"), Arguments.of(12, "(3,256,1,1)"),
                Arguments.of(13, "(4,256)"), Arguments.of(14, "(4,256,1)"), Arguments.of(15, "(4,256,0,1)"), Arguments.of(16, "(4,256,1,1)"),
                Arguments.of(17, "(5,4)"), Arguments.of(18, "(5,4,1)"), Arguments.of(19, "(5,4,2)"), Arguments.of(20, "(5,16)"), Arguments.of(21, "(5,16,1)"),
                Arguments.of(22, "(5,16,2)"), Arguments.of(23, "(5,32)"), Arguments.of(24, "(5,32,1)"), Arguments.of(25, "(5,32,2)"),
                Arguments.of(26, "(5,64)"), Arguments.of(27, "(5,64,1)"), Arguments.of(28, "(5,64,2)"), Arguments.of(29, "(5,128)"),
                Arguments.of(30, "(5,128,1)"), Arguments.of(31, "(5,128,2)"), Arguments.of(32, "(5,4,0,1)"), Arguments.of(33, "(5,4,1,1)"),
                Arguments.of(34, "(5,4,2,1)"), Arguments.of(35, "(5,16,0,1)"), Arguments.of(36, "(5,16,1,1)"), Arguments.of(37, "(5,16,2,1)"),
                Arguments.of(38, "(5,32,0,1)"), Arguments.of(39, "(5,32,1,1)"), Arguments.of(40, "(5,32,2,1)"), Arguments.of(41, "(5,64,0,1)"),
                Arguments.of(42, "(5,64,1,1)"), Arguments.of(43, "(5,64,2,1)"), Arguments.of(44, "(5,128,0,1)"), Arguments.of(45, "(5,128,1,1)"),
                Arguments.of(46, "(5,128,2,1)"), Arguments.of(47, "(2,192)"), Arguments.of(48, "(2,224)"), Arguments.of(49, "(2,240)"),
                Arguments.of(50, "(2,248)"), Arguments.of(51, "(2,252)"), Arguments.of(52, "(2,8,0,1)"), Arguments.of(53, "(2,8,1,1)"),
                Arguments.of(54, "(2,16,0,1)"), Arguments.of(55, "(2,16,1,1)"), Arguments.of(56, "(2,32,0,1)"), Arguments.of(57, "(2,32,1,1)"),
                Arguments.of(58, "(2,64,0,1)"), Arguments.of(59, "(2,64,1,1)"), Arguments.of(60, "(2,128,0,1)"), Arguments.of(61, "(2,128,1,1)"),
                Arguments.of(62, "(2,192,0,1)"), Arguments.of(63, "(2,192,1,1)"), Arguments.of(64, "(2,224,0,1)"), Arguments.of(65, "(2,224,1,1)"),
                Arguments.of(66, "(2,240,0,1)"), Arguments.of(67, "(2,240,1,1)"), Arguments.of(68, "(2,248,0,1)"), Arguments.of(69, "(2,248,1,1)"),
                Arguments.of(70, "(3,192)"), Arguments.of(71, "(3,224)"), Arguments.of(72, "(3,240)"), Arguments.of(73, "(3,248)"), Arguments.of(74, "(3,252)"),
                Arguments.of(75, "(3,8,0,1)"), Arguments.of(76, "(3,8,1,1)"), Arguments.of(77, "(3,16,0,1)"), Arguments.of(78, "(3,16,1,1)"),
                Arguments.of(79, "(3,32,0,1)"), Arguments.of(80, "(3,32,1,1)"), Arguments.of(81, "(3,64,0,1)"), Arguments.of(82, "(3,64,1,1)"),
                Arguments.of(83, "(3,128,0,1)"), Arguments.of(84, "(3,128,1,1)"), Arguments.of(85, "(3,192,0,1)"), Arguments.of(86, "(3,192,1,1)"),
                Arguments.of(87, "(3,224,0,1)"), Arguments.of(88, "(3,224,1,1)"), Arguments.of(89, "(3,240,0,1)"), Arguments.of(90, "(3,240,1,1)"),
                Arguments.of(91, "(3,248,0,1)"), Arguments.of(92, "(3,248,1,1)"), Arguments.of(93, "(4,192)"), Arguments.of(94, "(4,224)"),
                Arguments.of(95, "(4,240)"), Arguments.of(96, "(4,248)"), Arguments.of(97, "(4,252)"), Arguments.of(98, "(4,8,0,1)"),
                Arguments.of(99, "(4,8,1,1)"), Arguments.of(100, "(4,16,0,1)"), Arguments.of(101, "(4,16,1,1)"), Arguments.of(102, "(4,32,0,1)"),
                Arguments.of(103, "(4,32,1,1)"), Arguments.of(104, "(4,64,0,1)"), Arguments.of(105, "(4,64,1,1)"), Arguments.of(106, "(4,128,0,1)"),
                Arguments.of(107, "(4,128,1,1)"), Arguments.of(108, "(4,192,0,1)"), Arguments.of(109, "(4,192,1,1)"), Arguments.of(110, "(4,224,0,1)"),
                Arguments.of(111, "(4,224,1,1)"), Arguments.of(112, "(4,240,0,1)"), Arguments.of(113, "(4,240,1,1)"), Arguments.of(114, "(4,248,0,1)"),
                Arguments.of(115, "(4,248,1,1)")
        );
    }

    // Canonical specifiers to verify round-trip encoding of the index itself.
    static Stream<Arguments> canonicalGetSpecifier() {
        return IntStream.rangeClosed(1, SPECIFIER_CANONICAL_MAX).mapToObj(Arguments::of);
    }

    static Stream<Arguments> specifier() {
        return Stream.of(
                Arguments.of(new BHSDCodec(2, 125, 0, 1)),
                Arguments.of(new BHSDCodec(3, 125, 2, 1)),
                Arguments.of(new BHSDCodec(4, 125)),
                Arguments.of(new BHSDCodec(5, 125, 2, 0)),
                Arguments.of(new BHSDCodec(3, 5, 2, 1))
        );
    }

    // Tests

    @ParameterizedTest
    @MethodSource("arbitraryCodec")
    void testArbitraryCodec(final String expected, final byte[] bytes) throws IOException, Pack200Exception {
        try (InputStream in = new ByteArrayInputStream(bytes)) {
            assertEquals(expected, CodecEncoding.getCodec(SPECIFIER_ARBITRARY_BHSD, in, null).toString(),
                    "Arbitrary BHSD codec did not decode to the expected descriptor");
        }
    }

    @ParameterizedTest
    @MethodSource("canonicalEncodings")
    void testCanonicalEncodings(final int specifier, final String expectedCodec) throws IOException, Pack200Exception {
        assertEquals(expectedCodec, CodecEncoding.getCodec(specifier, null, null).toString(),
                "Canonical codec " + specifier + " did not match the expected descriptor");
    }

    @ParameterizedTest
    @MethodSource("canonicalGetSpecifier")
    void testCanonicalGetSpecifier(final int specifier) throws Pack200Exception, IOException {
        assertEquals(specifier, CodecEncoding.getSpecifier(CodecEncoding.getCodec(specifier, null, null), null)[0],
                "Canonical specifier round-trip failed for " + specifier);
    }

    @Test
    void testDefaultCodec() throws Pack200Exception, IOException {
        final Codec defaultCodec = new BHSDCodec(2, 16, 0, 0);
        assertEquals(defaultCodec, CodecEncoding.getCodec(0, null, defaultCodec),
                "Value 0 should resolve to the provided default codec");
    }

    @Test
    void testGetSpecifierForPopulationCodec() throws IOException, Pack200Exception {
        final PopulationCodec original = new PopulationCodec(Codec.BYTE1, Codec.CHAR3, Codec.UNSIGNED5);

        final int[] specifiers = CodecEncoding.getSpecifier(original, null);
        assertInclusiveRange(specifiers[0], SPECIFIER_POP_MIN, SPECIFIER_POP_MAX, "PopulationCodec");

        final PopulationCodec roundTrip = (PopulationCodec) decodeFromSpecifiers(specifiers, null);

        assertEquals(original.getFavouredCodec(), roundTrip.getFavouredCodec(), "Favoured codec mismatch");
        assertEquals(original.getTokenCodec(), roundTrip.getTokenCodec(), "Token codec mismatch");
        assertEquals(original.getUnfavouredCodec(), roundTrip.getUnfavouredCodec(), "Unfavoured codec mismatch");
    }

    @Test
    void testGetSpecifierForRunCodec() throws Pack200Exception, IOException {
        // Basic RunCodec round-trip
        RunCodec runCodec = new RunCodec(25, Codec.DELTA5, Codec.BYTE1);
        int[] specifiers = CodecEncoding.getSpecifier(runCodec, null);
        assertInclusiveRange(specifiers[0], SPECIFIER_RUN_MIN, SPECIFIER_RUN_MAX, "RunCodec");
        RunCodec roundTrip = (RunCodec) decodeFromSpecifiers(specifiers, null);
        assertEquals(runCodec.getK(), roundTrip.getK(), "Run length (k) mismatch");
        assertEquals(runCodec.getACodec(), roundTrip.getACodec(), "A codec mismatch");
        assertEquals(runCodec.getBCodec(), roundTrip.getBCodec(), "B codec mismatch");

        // One codec is the same as the default
        runCodec = new RunCodec(4096, Codec.DELTA5, Codec.BYTE1);
        specifiers = CodecEncoding.getSpecifier(runCodec, Codec.DELTA5);
        assertInclusiveRange(specifiers[0], SPECIFIER_RUN_MIN, SPECIFIER_RUN_MAX, "RunCodec with default A");
        roundTrip = (RunCodec) decodeFromSpecifiers(specifiers, Codec.DELTA5);
        assertEquals(runCodec.getK(), roundTrip.getK(), "Run length (k) mismatch with default A");
        assertEquals(runCodec.getACodec(), roundTrip.getACodec(), "A codec mismatch with default A");
        assertEquals(runCodec.getBCodec(), roundTrip.getBCodec(), "B codec mismatch with default A");

        // Nested run codecs
        runCodec = new RunCodec(64, Codec.SIGNED5, new RunCodec(25, Codec.UDELTA5, Codec.DELTA5));
        specifiers = CodecEncoding.getSpecifier(runCodec, null);
        assertInclusiveRange(specifiers[0], SPECIFIER_RUN_MIN, SPECIFIER_RUN_MAX, "Nested RunCodec");
        roundTrip = (RunCodec) decodeFromSpecifiers(specifiers, null);
        assertEquals(runCodec.getK(), roundTrip.getK(), "Outer run length (k) mismatch");
        assertEquals(runCodec.getACodec(), roundTrip.getACodec(), "Outer A codec mismatch");
        RunCodec bCodec = (RunCodec) runCodec.getBCodec();
        RunCodec bCodec2 = (RunCodec) roundTrip.getBCodec();
        assertEquals(bCodec.getK(), bCodec2.getK(), "Inner run length (k) mismatch");
        assertEquals(bCodec.getACodec(), bCodec2.getACodec(), "Inner A codec mismatch");
        assertEquals(bCodec.getBCodec(), bCodec2.getBCodec(), "Inner B codec mismatch");

        // Nested with one the same as the default
        runCodec = new RunCodec(64, Codec.SIGNED5, new RunCodec(25, Codec.UDELTA5, Codec.DELTA5));
        specifiers = CodecEncoding.getSpecifier(runCodec, Codec.UDELTA5);
        assertInclusiveRange(specifiers[0], SPECIFIER_RUN_MIN, SPECIFIER_RUN_MAX, "Nested RunCodec with default");
        roundTrip = (RunCodec) decodeFromSpecifiers(specifiers, Codec.UDELTA5);
        assertEquals(runCodec.getK(), roundTrip.getK(), "Outer run length (k) mismatch with default");
        assertEquals(runCodec.getACodec(), roundTrip.getACodec(), "Outer A codec mismatch with default");
        bCodec = (RunCodec) runCodec.getBCodec();
        bCodec2 = (RunCodec) roundTrip.getBCodec();
        assertEquals(bCodec.getK(), bCodec2.getK(), "Inner run length (k) mismatch with default");
        assertEquals(bCodec.getACodec(), bCodec2.getACodec(), "Inner A codec mismatch with default");
        assertEquals(bCodec.getBCodec(), bCodec2.getBCodec(), "Inner B codec mismatch with default");
    }

    @ParameterizedTest
    @MethodSource("specifier")
    void testGetSpecifier(final Codec codec) throws IOException, Pack200Exception {
        final int[] specifiers = CodecEncoding.getSpecifier(codec, null);
        assertEquals(3, specifiers.length, "Arbitrary BHSD specifier should be 3 ints (tag + 2 bytes)");
        assertEquals(SPECIFIER_ARBITRARY_BHSD, specifiers[0], "Arbitrary BHSD specifier tag should be 116");

        final Codec decoded = decodeFromSpecifiers(specifiers, null);
        assertEquals(codec, decoded, "Arbitrary BHSD codec did not round-trip");
    }
}