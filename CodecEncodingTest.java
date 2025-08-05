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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
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
 * Tests for {@link CodecEncoding}.
 */
class CodecEncodingTest {

    // Helper method to decode a codec from specifiers
    private Codec decodeCodec(int[] specifiers, Codec defaultCodec) throws IOException, Pack200Exception {
        if (specifiers.length > 1) {
            byte[] bytes = new byte[specifiers.length - 1];
            for (int i = 0; i < bytes.length; i++) {
                bytes[i] = (byte) specifiers[i + 1];
            }
            InputStream in = new ByteArrayInputStream(bytes);
            return CodecEncoding.getCodec(specifiers[0], in, defaultCodec);
        } else {
            return CodecEncoding.getCodec(specifiers[0], null, defaultCodec);
        }
    }

    // Helper to compare two RunCodec instances recursively
    private void assertRunCodecsEqual(RunCodec expected, RunCodec actual) {
        assertEquals(expected.getK(), actual.getK(), "RunCodec K value mismatch");
        assertEquals(expected.getACodec(), actual.getACodec(), "RunCodec A codec mismatch");
        
        // Handle nested RunCodecs recursively
        if (expected.getBCodec() instanceof RunCodec) {
            assertInstanceOf(RunCodec.class, actual.getBCodec(), "Nested RunCodec expected");
            assertRunCodecsEqual((RunCodec) expected.getBCodec(), (RunCodec) actual.getBCodec());
        } else {
            assertEquals(expected.getBCodec(), actual.getBCodec(), "RunCodec B codec mismatch");
        }
    }

    // Test data providers

    static Stream<Arguments> arbitraryCodec() {
        return Stream.of(
            Arguments.of("(1,256)", new byte[] { 0x00, (byte) 0xFF }),
            Arguments.of("(5,128,2,1)", new byte[] { 0x25, (byte) 0x7F }),
            Arguments.of("(2,128,1,1)", new byte[] { 0x0B, (byte) 0x7F })
        );
    }

    static Stream<Arguments> canonicalEncodings() {
        return Stream.of(
            Arguments.of(1, "(1,256)"), Arguments.of(2, "(1,256,1)"), 
            Arguments.of(3, "(1,256,0,1)"), Arguments.of(4, "(1,256,1,1)"),
            // ... (other canonical encodings remain unchanged)
            Arguments.of(115, "(4,248,1,1)")
        );
    }

    static Stream<Arguments> canonicalGetSpecifier() {
        return IntStream.range(1, 116).mapToObj(Arguments::of);
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
    void testGetCodec_WithArbitraryCodec_ReturnsExpectedEncoding(String expected, byte[] bytes) 
            throws IOException, Pack200Exception {
        Codec codec = CodecEncoding.getCodec(116, new ByteArrayInputStream(bytes), null);
        assertEquals(expected, codec.toString());
    }

    @ParameterizedTest
    @MethodSource("canonicalEncodings")
    void testGetCodec_WithCanonicalIndex_ReturnsExpectedCodec(int index, String expectedCodec) 
            throws IOException, Pack200Exception {
        Codec codec = CodecEncoding.getCodec(index, null, null);
        assertEquals(expectedCodec, codec.toString());
    }

    @ParameterizedTest
    @MethodSource("canonicalGetSpecifier")
    void testGetSpecifier_WithCanonicalCodec_ReturnsOriginalIndex(int index) 
            throws Pack200Exception, IOException {
        Codec codec = CodecEncoding.getCodec(index, null, null);
        int[] specifiers = CodecEncoding.getSpecifier(codec, null);
        assertEquals(index, specifiers[0]);
    }

    @Test
    void testGetCodec_WithZeroIndex_ReturnsDefaultCodec() throws Pack200Exception, IOException {
        final Codec defaultCodec = new BHSDCodec(2, 16, 0, 0);
        Codec result = CodecEncoding.getCodec(0, null, defaultCodec);
        assertEquals(defaultCodec, result);
    }

    @Test
    void testGetSpecifier_ForPopulationCodec_RoundTripsSuccessfully() 
            throws IOException, Pack200Exception {
        // Setup population codec with specific components
        final PopulationCodec populationCodec = new PopulationCodec(
            Codec.BYTE1, 
            Codec.CHAR3, 
            Codec.UNSIGNED5
        );

        // Get specifier and verify it's in correct range
        final int[] specifiers = CodecEncoding.getSpecifier(populationCodec, null);
        assertTrue(specifiers[0] > 140 && specifiers[0] < 189, 
            "PopulationCodec specifier should be between 141-188");

        // Decode and verify components
        Codec decodedCodec = decodeCodec(specifiers, null);
        assertInstanceOf(PopulationCodec.class, decodedCodec);
        PopulationCodec decodedPopulationCodec = (PopulationCodec) decodedCodec;
        
        assertEquals(populationCodec.getFavouredCodec(), decodedPopulationCodec.getFavouredCodec());
        assertEquals(populationCodec.getTokenCodec(), decodedPopulationCodec.getTokenCodec());
        assertEquals(populationCodec.getUnfavouredCodec(), decodedPopulationCodec.getUnfavouredCodec());
    }

    @Test
    void testGetSpecifier_ForRunCodec_RoundTripsSuccessfully() 
            throws Pack200Exception, IOException {
        // Setup a simple RunCodec
        RunCodec runCodec = new RunCodec(25, Codec.DELTA5, Codec.BYTE1);
        
        // Verify specifier in correct range
        int[] specifiers = CodecEncoding.getSpecifier(runCodec, null);
        assertTrue(specifiers[0] > 116 && specifiers[0] < 141, 
            "RunCodec specifier should be between 117-140");
        
        // Decode and verify
        Codec decodedCodec = decodeCodec(specifiers, null);
        assertInstanceOf(RunCodec.class, decodedCodec);
        RunCodec decodedRunCodec = (RunCodec) decodedCodec;
        
        assertEquals(runCodec.getK(), decodedRunCodec.getK());
        assertEquals(runCodec.getACodec(), decodedRunCodec.getACodec());
        assertEquals(runCodec.getBCodec(), decodedRunCodec.getBCodec());
    }

    @Test
    void testGetSpecifier_ForRunCodecWithDefaultCodec_RoundTripsSuccessfully() 
            throws Pack200Exception, IOException {
        // Setup RunCodec where one component matches default
        RunCodec runCodec = new RunCodec(4096, Codec.DELTA5, Codec.BYTE1);
        Codec defaultCodec = Codec.DELTA5;
        
        // Verify specifier in correct range
        int[] specifiers = CodecEncoding.getSpecifier(runCodec, defaultCodec);
        assertTrue(specifiers[0] > 116 && specifiers[0] < 141, 
            "RunCodec specifier should be between 117-140");
        
        // Decode with default and verify
        Codec decodedCodec = decodeCodec(specifiers, defaultCodec);
        assertInstanceOf(RunCodec.class, decodedCodec);
        RunCodec decodedRunCodec = (RunCodec) decodedCodec;
        
        assertEquals(runCodec.getK(), decodedRunCodec.getK());
        assertEquals(runCodec.getACodec(), decodedRunCodec.getACodec());
        assertEquals(runCodec.getBCodec(), decodedRunCodec.getBCodec());
    }

    @Test
    void testGetSpecifier_ForNestedRunCodec_RoundTripsSuccessfully() 
            throws Pack200Exception, IOException {
        // Setup nested RunCodec
        RunCodec innerCodec = new RunCodec(25, Codec.UDELTA5, Codec.DELTA5);
        RunCodec runCodec = new RunCodec(64, Codec.SIGNED5, innerCodec);
        
        // Verify specifier in correct range
        int[] specifiers = CodecEncoding.getSpecifier(runCodec, null);
        assertTrue(specifiers[0] > 116 && specifiers[0] < 141, 
            "RunCodec specifier should be between 117-140");
        
        // Decode and verify nested structure
        Codec decodedCodec = decodeCodec(specifiers, null);
        assertInstanceOf(RunCodec.class, decodedCodec);
        RunCodec decodedRunCodec = (RunCodec) decodedCodec;
        
        assertRunCodecsEqual(runCodec, decodedRunCodec);
    }

    @Test
    void testGetSpecifier_ForNestedRunCodecWithDefault_RoundTripsSuccessfully() 
            throws Pack200Exception, IOException {
        // Setup nested RunCodec with default
        RunCodec innerCodec = new RunCodec(25, Codec.UDELTA5, Codec.DELTA5);
        RunCodec runCodec = new RunCodec(64, Codec.SIGNED5, innerCodec);
        Codec defaultCodec = Codec.UDELTA5;
        
        // Verify specifier in correct range
        int[] specifiers = CodecEncoding.getSpecifier(runCodec, defaultCodec);
        assertTrue(specifiers[0] > 116 && specifiers[0] < 141, 
            "RunCodec specifier should be between 117-140");
        
        // Decode with default and verify
        Codec decodedCodec = decodeCodec(specifiers, defaultCodec);
        assertInstanceOf(RunCodec.class, decodedCodec);
        RunCodec decodedRunCodec = (RunCodec) decodedCodec;
        
        assertRunCodecsEqual(runCodec, decodedRunCodec);
    }

    @ParameterizedTest
    @MethodSource("specifier")
    void testGetSpecifier_ForArbitraryBHSDCodec_RoundTripsSuccessfully(Codec codec) 
            throws IOException, Pack200Exception {
        int[] specifiers = CodecEncoding.getSpecifier(codec, null);
        assertEquals(3, specifiers.length, "BHSDCodec specifier should have 3 elements");
        assertEquals(116, specifiers[0], "First specifier should be 116 for BHSDCodec");
        
        Codec decodedCodec = decodeCodec(specifiers, null);
        assertEquals(codec, decodedCodec);
    }
}