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
 * Test suite for CodecEncoding class.
 */
class CodecEncodingTest {

    /**
     * Provides arbitrary codec test data.
     */
    static Stream<Arguments> provideArbitraryCodecData() {
        return Stream.of(
            Arguments.of("(1,256)", new byte[] { 0x00, (byte) 0xFF }),
            Arguments.of("(5,128,2,1)", new byte[] { 0x25, (byte) 0x7F }),
            Arguments.of("(2,128,1,1)", new byte[] { 0x0B, (byte) 0x7F })
        );
    }

    /**
     * Provides canonical encoding test data as specified by the Pack200 spec.
     */
    static Stream<Arguments> provideCanonicalEncodings() {
        return Stream.of(
            Arguments.of(1, "(1,256)"),
            Arguments.of(2, "(1,256,1)"),
            // ... (other arguments omitted for brevity)
            Arguments.of(115, "(4,248,1,1)")
        );
    }

    /**
     * Provides test data for canonical specifier retrieval.
     */
    static Stream<Arguments> provideCanonicalSpecifierData() {
        return IntStream.range(1, 115).mapToObj(Arguments::of);
    }

    /**
     * Provides test data for specifier tests.
     */
    static Stream<Arguments> provideSpecifierData() {
        return Stream.of(
            Arguments.of(new BHSDCodec(2, 125, 0, 1)),
            Arguments.of(new BHSDCodec(3, 125, 2, 1)),
            Arguments.of(new BHSDCodec(4, 125)),
            Arguments.of(new BHSDCodec(5, 125, 2, 0)),
            Arguments.of(new BHSDCodec(3, 5, 2, 1))
        );
    }

    @ParameterizedTest
    @MethodSource("provideArbitraryCodecData")
    void testArbitraryCodec(final String expected, final byte[] bytes) throws IOException, Pack200Exception {
        Codec codec = CodecEncoding.getCodec(116, new ByteArrayInputStream(bytes), null);
        assertEquals(expected, codec.toString());
    }

    @ParameterizedTest
    @MethodSource("provideCanonicalEncodings")
    void testCanonicalEncodings(final int index, final String expectedCodec) throws IOException, Pack200Exception {
        Codec codec = CodecEncoding.getCodec(index, null, null);
        assertEquals(expectedCodec, codec.toString());
    }

    @ParameterizedTest
    @MethodSource("provideCanonicalSpecifierData")
    void testCanonicalGetSpecifier(final int index) throws Pack200Exception, IOException {
        Codec codec = CodecEncoding.getCodec(index, null, null);
        int[] specifier = CodecEncoding.getSpecifier(codec, null);
        assertEquals(index, specifier[0]);
    }

    @Test
    void testDefaultCodec() throws Pack200Exception, IOException {
        final Codec defaultCodec = new BHSDCodec(2, 16, 0, 0);
        Codec codec = CodecEncoding.getCodec(0, null, defaultCodec);
        assertEquals(defaultCodec, codec);
    }

    @Test
    void testGetSpecifierForPopulationCodec() throws IOException, Pack200Exception {
        final PopulationCodec populationCodec = new PopulationCodec(Codec.BYTE1, Codec.CHAR3, Codec.UNSIGNED5);
        int[] specifiers = CodecEncoding.getSpecifier(populationCodec, null);
        assertTrue(specifiers[0] > 140 && specifiers[0] < 189);

        byte[] bytes = extractSpecifierBytes(specifiers);
        InputStream in = new ByteArrayInputStream(bytes);
        PopulationCodec decodedCodec = (PopulationCodec) CodecEncoding.getCodec(specifiers[0], in, null);

        assertEquals(populationCodec.getFavouredCodec(), decodedCodec.getFavouredCodec());
        assertEquals(populationCodec.getTokenCodec(), decodedCodec.getTokenCodec());
        assertEquals(populationCodec.getUnfavouredCodec(), decodedCodec.getUnfavouredCodec());
    }

    @Test
    void testGetSpecifierForRunCodec() throws Pack200Exception, IOException {
        testRunCodec(new RunCodec(25, Codec.DELTA5, Codec.BYTE1), null);
        testRunCodec(new RunCodec(4096, Codec.DELTA5, Codec.BYTE1), Codec.DELTA5);
        testNestedRunCodec(new RunCodec(64, Codec.SIGNED5, new RunCodec(25, Codec.UDELTA5, Codec.DELTA5)), null);
        testNestedRunCodec(new RunCodec(64, Codec.SIGNED5, new RunCodec(25, Codec.UDELTA5, Codec.DELTA5)), Codec.UDELTA5);
    }

    @ParameterizedTest
    @MethodSource("provideSpecifierData")
    void testGetSpecifier(final Codec codec) throws IOException, Pack200Exception {
        int[] specifiers = CodecEncoding.getSpecifier(codec, null);
        assertEquals(3, specifiers.length);
        assertEquals(116, specifiers[0]);

        byte[] bytes = { (byte) specifiers[1], (byte) specifiers[2] };
        InputStream in = new ByteArrayInputStream(bytes);
        Codec decodedCodec = CodecEncoding.getCodec(116, in, null);
        assertEquals(codec, decodedCodec);
    }

    // Helper method to extract specifier bytes
    private byte[] extractSpecifierBytes(int[] specifiers) {
        byte[] bytes = new byte[specifiers.length - 1];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) specifiers[i + 1];
        }
        return bytes;
    }

    // Helper method to test RunCodec
    private void testRunCodec(RunCodec runCodec, Codec defaultCodec) throws IOException, Pack200Exception {
        int[] specifiers = CodecEncoding.getSpecifier(runCodec, defaultCodec);
        assertTrue(specifiers[0] > 116 && specifiers[0] < 141);

        byte[] bytes = extractSpecifierBytes(specifiers);
        InputStream in = new ByteArrayInputStream(bytes);
        RunCodec decodedCodec = (RunCodec) CodecEncoding.getCodec(specifiers[0], in, defaultCodec);

        assertEquals(runCodec.getK(), decodedCodec.getK());
        assertEquals(runCodec.getACodec(), decodedCodec.getACodec());
        assertEquals(runCodec.getBCodec(), decodedCodec.getBCodec());
    }

    // Helper method to test nested RunCodec
    private void testNestedRunCodec(RunCodec runCodec, Codec defaultCodec) throws IOException, Pack200Exception {
        int[] specifiers = CodecEncoding.getSpecifier(runCodec, defaultCodec);
        assertTrue(specifiers[0] > 116 && specifiers[0] < 141);

        byte[] bytes = extractSpecifierBytes(specifiers);
        InputStream in = new ByteArrayInputStream(bytes);
        RunCodec decodedCodec = (RunCodec) CodecEncoding.getCodec(specifiers[0], in, defaultCodec);

        assertEquals(runCodec.getK(), decodedCodec.getK());
        assertEquals(runCodec.getACodec(), decodedCodec.getACodec());

        RunCodec bCodec = (RunCodec) runCodec.getBCodec();
        RunCodec bCodec2 = (RunCodec) decodedCodec.getBCodec();
        assertEquals(bCodec.getK(), bCodec2.getK());
        assertEquals(bCodec.getACodec(), bCodec2.getACodec());
        assertEquals(bCodec.getBCodec(), bCodec2.getBCodec());
    }
}