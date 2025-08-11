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
 * Tests for CodecEncoding class which handles Pack200 codec encoding/decoding.
 * 
 * Pack200 uses various encoding schemes:
 * - Canonical encodings (1-115): Predefined standard encodings
 * - Arbitrary encodings (116+): Custom encodings with additional byte headers
 * - Special codec types: Population, Run, and BHSD codecs
 */
class CodecEncodingTest {

    // Constants for codec specifier ranges
    private static final int ARBITRARY_CODEC_SPECIFIER = 116;
    private static final int RUN_CODEC_MIN_SPECIFIER = 117;
    private static final int RUN_CODEC_MAX_SPECIFIER = 140;
    private static final int POPULATION_CODEC_MIN_SPECIFIER = 141;
    private static final int POPULATION_CODEC_MAX_SPECIFIER = 188;

    /**
     * Test data for arbitrary (non-canonical) codecs that require additional byte headers.
     * Format: codec string representation -> byte array containing the header data
     */
    static Stream<Arguments> arbitraryCodecTestData() {
        return Stream.of(
            Arguments.of("(1,256)", new byte[] { 0x00, (byte) 0xFF }),
            Arguments.of("(5,128,2,1)", new byte[] { 0x25, (byte) 0x7F }),
            Arguments.of("(2,128,1,1)", new byte[] { 0x0B, (byte) 0x7F })
        );
    }

    /**
     * Test data for canonical encodings as specified by Pack200 specification.
     * These are predefined encodings that can be represented by a single specifier (1-115).
     */
    static Stream<Arguments> canonicalEncodingTestData() {
        return Stream.of(
            // Basic encodings (1-16)
            Arguments.of(1, "(1,256)"), Arguments.of(2, "(1,256,1)"), 
            Arguments.of(3, "(1,256,0,1)"), Arguments.of(4, "(1,256,1,1)"),
            Arguments.of(5, "(2,256)"), Arguments.of(6, "(2,256,1)"), 
            Arguments.of(7, "(2,256,0,1)"), Arguments.of(8, "(2,256,1,1)"),
            Arguments.of(9, "(3,256)"), Arguments.of(10, "(3,256,1)"), 
            Arguments.of(11, "(3,256,0,1)"), Arguments.of(12, "(3,256,1,1)"),
            Arguments.of(13, "(4,256)"), Arguments.of(14, "(4,256,1)"), 
            Arguments.of(15, "(4,256,0,1)"), Arguments.of(16, "(4,256,1,1)"),
            
            // Compact encodings (17-31)
            Arguments.of(17, "(5,4)"), Arguments.of(18, "(5,4,1)"), Arguments.of(19, "(5,4,2)"),
            Arguments.of(20, "(5,16)"), Arguments.of(21, "(5,16,1)"), Arguments.of(22, "(5,16,2)"),
            Arguments.of(23, "(5,32)"), Arguments.of(24, "(5,32,1)"), Arguments.of(25, "(5,32,2)"),
            Arguments.of(26, "(5,64)"), Arguments.of(27, "(5,64,1)"), Arguments.of(28, "(5,64,2)"),
            Arguments.of(29, "(5,128)"), Arguments.of(30, "(5,128,1)"), Arguments.of(31, "(5,128,2)"),
            
            // Signed compact encodings (32-46)
            Arguments.of(32, "(5,4,0,1)"), Arguments.of(33, "(5,4,1,1)"), Arguments.of(34, "(5,4,2,1)"),
            Arguments.of(35, "(5,16,0,1)"), Arguments.of(36, "(5,16,1,1)"), Arguments.of(37, "(5,16,2,1)"),
            Arguments.of(38, "(5,32,0,1)"), Arguments.of(39, "(5,32,1,1)"), Arguments.of(40, "(5,32,2,1)"),
            Arguments.of(41, "(5,64,0,1)"), Arguments.of(42, "(5,64,1,1)"), Arguments.of(43, "(5,64,2,1)"),
            Arguments.of(44, "(5,128,0,1)"), Arguments.of(45, "(5,128,1,1)"), Arguments.of(46, "(5,128,2,1)"),
            
            // Optimized 2-byte encodings (47-69)
            Arguments.of(47, "(2,192)"), Arguments.of(48, "(2,224)"), Arguments.of(49, "(2,240)"),
            Arguments.of(50, "(2,248)"), Arguments.of(51, "(2,252)"),
            Arguments.of(52, "(2,8,0,1)"), Arguments.of(53, "(2,8,1,1)"),
            Arguments.of(54, "(2,16,0,1)"), Arguments.of(55, "(2,16,1,1)"),
            Arguments.of(56, "(2,32,0,1)"), Arguments.of(57, "(2,32,1,1)"),
            Arguments.of(58, "(2,64,0,1)"), Arguments.of(59, "(2,64,1,1)"),
            Arguments.of(60, "(2,128,0,1)"), Arguments.of(61, "(2,128,1,1)"),
            Arguments.of(62, "(2,192,0,1)"), Arguments.of(63, "(2,192,1,1)"),
            Arguments.of(64, "(2,224,0,1)"), Arguments.of(65, "(2,224,1,1)"),
            Arguments.of(66, "(2,240,0,1)"), Arguments.of(67, "(2,240,1,1)"),
            Arguments.of(68, "(2,248,0,1)"), Arguments.of(69, "(2,248,1,1)"),
            
            // Optimized 3-byte encodings (70-92)
            Arguments.of(70, "(3,192)"), Arguments.of(71, "(3,224)"), Arguments.of(72, "(3,240)"),
            Arguments.of(73, "(3,248)"), Arguments.of(74, "(3,252)"),
            Arguments.of(75, "(3,8,0,1)"), Arguments.of(76, "(3,8,1,1)"),
            Arguments.of(77, "(3,16,0,1)"), Arguments.of(78, "(3,16,1,1)"),
            Arguments.of(79, "(3,32,0,1)"), Arguments.of(80, "(3,32,1,1)"),
            Arguments.of(81, "(3,64,0,1)"), Arguments.of(82, "(3,64,1,1)"),
            Arguments.of(83, "(3,128,0,1)"), Arguments.of(84, "(3,128,1,1)"),
            Arguments.of(85, "(3,192,0,1)"), Arguments.of(86, "(3,192,1,1)"),
            Arguments.of(87, "(3,224,0,1)"), Arguments.of(88, "(3,224,1,1)"),
            Arguments.of(89, "(3,240,0,1)"), Arguments.of(90, "(3,240,1,1)"),
            Arguments.of(91, "(3,248,0,1)"), Arguments.of(92, "(3,248,1,1)"),
            
            // Optimized 4-byte encodings (93-115)
            Arguments.of(93, "(4,192)"), Arguments.of(94, "(4,224)"), Arguments.of(95, "(4,240)"),
            Arguments.of(96, "(4,248)"), Arguments.of(97, "(4,252)"),
            Arguments.of(98, "(4,8,0,1)"), Arguments.of(99, "(4,8,1,1)"),
            Arguments.of(100, "(4,16,0,1)"), Arguments.of(101, "(4,16,1,1)"),
            Arguments.of(102, "(4,32,0,1)"), Arguments.of(103, "(4,32,1,1)"),
            Arguments.of(104, "(4,64,0,1)"), Arguments.of(105, "(4,64,1,1)"),
            Arguments.of(106, "(4,128,0,1)"), Arguments.of(107, "(4,128,1,1)"),
            Arguments.of(108, "(4,192,0,1)"), Arguments.of(109, "(4,192,1,1)"),
            Arguments.of(110, "(4,224,0,1)"), Arguments.of(111, "(4,224,1,1)"),
            Arguments.of(112, "(4,240,0,1)"), Arguments.of(113, "(4,240,1,1)"),
            Arguments.of(114, "(4,248,0,1)"), Arguments.of(115, "(4,248,1,1)")
        );
    }

    /**
     * Generates test data for all canonical codec specifiers (1-114).
     */
    static Stream<Arguments> allCanonicalSpecifiers() {
        return IntStream.range(1, 115).mapToObj(Arguments::of);
    }

    /**
     * Test data for BHSD codec specifier generation.
     * These are custom codecs that should generate specifier 116 (arbitrary codec).
     */
    static Stream<Arguments> bhsdCodecTestData() {
        return Stream.of(
            Arguments.of(new BHSDCodec(2, 125, 0, 1)),
            Arguments.of(new BHSDCodec(3, 125, 2, 1)),
            Arguments.of(new BHSDCodec(4, 125)),
            Arguments.of(new BHSDCodec(5, 125, 2, 0)),
            Arguments.of(new BHSDCodec(3, 5, 2, 1))
        );
    }

    @ParameterizedTest
    @MethodSource("arbitraryCodecTestData")
    void shouldDecodeArbitraryCodecsFromByteHeaders(String expectedCodecString, byte[] headerBytes) 
            throws IOException, Pack200Exception {
        // Given: An arbitrary codec specifier (116) and header bytes
        InputStream headerStream = new ByteArrayInputStream(headerBytes);
        
        // When: Getting the codec from the encoding
        Codec actualCodec = CodecEncoding.getCodec(ARBITRARY_CODEC_SPECIFIER, headerStream, null);
        
        // Then: The codec should match the expected string representation
        assertEquals(expectedCodecString, actualCodec.toString());
    }

    @ParameterizedTest
    @MethodSource("canonicalEncodingTestData")
    void shouldReturnCorrectCanonicalCodecForSpecifier(int specifier, String expectedCodecString) 
            throws IOException, Pack200Exception {
        // Given: A canonical codec specifier (1-115)
        // When: Getting the codec (no input stream needed for canonical codecs)
        Codec actualCodec = CodecEncoding.getCodec(specifier, null, null);
        
        // Then: The codec should match the expected canonical encoding
        assertEquals(expectedCodecString, actualCodec.toString());
    }

    @ParameterizedTest
    @MethodSource("allCanonicalSpecifiers")
    void shouldGenerateCorrectSpecifierForCanonicalCodecs(int expectedSpecifier) 
            throws Pack200Exception, IOException {
        // Given: A canonical codec retrieved by its specifier
        Codec codec = CodecEncoding.getCodec(expectedSpecifier, null, null);
        
        // When: Getting the specifier for that codec
        int[] specifiers = CodecEncoding.getSpecifier(codec, null);
        int actualSpecifier = specifiers[0];
        
        // Then: The specifier should match the original
        assertEquals(expectedSpecifier, actualSpecifier);
    }

    @Test
    void shouldReturnDefaultCodecWhenSpecifierIsZero() throws Pack200Exception, IOException {
        // Given: A default codec and specifier 0
        Codec defaultCodec = new BHSDCodec(2, 16, 0, 0);
        
        // When: Getting codec with specifier 0
        Codec actualCodec = CodecEncoding.getCodec(0, null, defaultCodec);
        
        // Then: Should return the default codec
        assertEquals(defaultCodec, actualCodec);
    }

    @ParameterizedTest
    @MethodSource("bhsdCodecTestData")
    void shouldGenerateSpecifier116ForCustomBHSDCodecs(Codec customCodec) 
            throws IOException, Pack200Exception {
        // Given: A custom BHSD codec
        // When: Getting its specifier
        int[] specifiers = CodecEncoding.getSpecifier(customCodec, null);
        
        // Then: Should generate specifier 116 (arbitrary codec) and 2 additional bytes
        assertEquals(3, specifiers.length, "Should have 3 elements: specifier + 2 header bytes");
        assertEquals(ARBITRARY_CODEC_SPECIFIER, specifiers[0], "Should use arbitrary codec specifier");
        
        // And: Should be able to reconstruct the codec from the specifier and bytes
        byte[] headerBytes = {(byte) specifiers[1], (byte) specifiers[2]};
        InputStream headerStream = new ByteArrayInputStream(headerBytes);
        Codec reconstructedCodec = CodecEncoding.getCodec(ARBITRARY_CODEC_SPECIFIER, headerStream, null);
        
        assertEquals(customCodec, reconstructedCodec, "Reconstructed codec should match original");
    }

    @Test
    void shouldHandlePopulationCodecEncodingAndDecoding() throws IOException, Pack200Exception {
        // Given: A population codec with three different sub-codecs
        PopulationCodec originalCodec = new PopulationCodec(Codec.BYTE1, Codec.CHAR3, Codec.UNSIGNED5);
        
        // When: Getting its specifier
        int[] specifiers = CodecEncoding.getSpecifier(originalCodec, null);
        int specifier = specifiers[0];
        
        // Then: Should be in the population codec range
        assertTrue(specifier > POPULATION_CODEC_MIN_SPECIFIER && specifier < POPULATION_CODEC_MAX_SPECIFIER,
                "Population codec specifier should be in range 141-188");
        
        // And: Should be able to reconstruct the codec
        byte[] headerBytes = extractHeaderBytes(specifiers);
        InputStream headerStream = new ByteArrayInputStream(headerBytes);
        PopulationCodec reconstructedCodec = (PopulationCodec) CodecEncoding.getCodec(specifier, headerStream, null);
        
        // And: All sub-codecs should match
        assertEquals(originalCodec.getFavouredCodec(), reconstructedCodec.getFavouredCodec());
        assertEquals(originalCodec.getTokenCodec(), reconstructedCodec.getTokenCodec());
        assertEquals(originalCodec.getUnfavouredCodec(), reconstructedCodec.getUnfavouredCodec());
    }

    @Test
    void shouldHandleRunCodecEncodingAndDecoding() throws Pack200Exception, IOException {
        // Given: A simple run codec
        RunCodec originalCodec = new RunCodec(25, Codec.DELTA5, Codec.BYTE1);
        
        // When: Getting its specifier
        int[] specifiers = CodecEncoding.getSpecifier(originalCodec, null);
        int specifier = specifiers[0];
        
        // Then: Should be in the run codec range
        assertTrue(specifier > RUN_CODEC_MIN_SPECIFIER && specifier < RUN_CODEC_MAX_SPECIFIER,
                "Run codec specifier should be in range 117-140");
        
        // And: Should be able to reconstruct the codec
        byte[] headerBytes = extractHeaderBytes(specifiers);
        InputStream headerStream = new ByteArrayInputStream(headerBytes);
        RunCodec reconstructedCodec = (RunCodec) CodecEncoding.getCodec(specifier, headerStream, null);
        
        // And: All properties should match
        assertRunCodecsEqual(originalCodec, reconstructedCodec);
    }

    @Test
    void shouldHandleRunCodecWithDefaultCodecOptimization() throws Pack200Exception, IOException {
        // Given: A run codec where one sub-codec matches the default
        RunCodec originalCodec = new RunCodec(4096, Codec.DELTA5, Codec.BYTE1);
        Codec defaultCodec = Codec.DELTA5; // Same as A codec
        
        // When: Getting specifier with default codec specified
        int[] specifiers = CodecEncoding.getSpecifier(originalCodec, defaultCodec);
        
        // Then: Should still be able to reconstruct correctly
        byte[] headerBytes = extractHeaderBytes(specifiers);
        InputStream headerStream = new ByteArrayInputStream(headerBytes);
        RunCodec reconstructedCodec = (RunCodec) CodecEncoding.getCodec(specifiers[0], headerStream, defaultCodec);
        
        assertRunCodecsEqual(originalCodec, reconstructedCodec);
    }

    @Test
    void shouldHandleNestedRunCodecs() throws Pack200Exception, IOException {
        // Given: A run codec with another run codec as its B codec
        RunCodec innerRunCodec = new RunCodec(25, Codec.UDELTA5, Codec.DELTA5);
        RunCodec outerRunCodec = new RunCodec(64, Codec.SIGNED5, innerRunCodec);
        
        // When: Getting specifier and reconstructing
        int[] specifiers = CodecEncoding.getSpecifier(outerRunCodec, null);
        byte[] headerBytes = extractHeaderBytes(specifiers);
        InputStream headerStream = new ByteArrayInputStream(headerBytes);
        RunCodec reconstructedCodec = (RunCodec) CodecEncoding.getCodec(specifiers[0], headerStream, null);
        
        // Then: Outer codec should match
        assertEquals(outerRunCodec.getK(), reconstructedCodec.getK());
        assertEquals(outerRunCodec.getACodec(), reconstructedCodec.getACodec());
        
        // And: Inner codec should also match
        RunCodec reconstructedInner = (RunCodec) reconstructedCodec.getBCodec();
        RunCodec originalInner = (RunCodec) outerRunCodec.getBCodec();
        assertRunCodecsEqual(originalInner, reconstructedInner);
    }

    @Test
    void shouldHandleNestedRunCodecsWithDefaultOptimization() throws Pack200Exception, IOException {
        // Given: Nested run codecs with a default codec that matches one of the inner codecs
        RunCodec innerRunCodec = new RunCodec(25, Codec.UDELTA5, Codec.DELTA5);
        RunCodec outerRunCodec = new RunCodec(64, Codec.SIGNED5, innerRunCodec);
        Codec defaultCodec = Codec.UDELTA5; // Matches inner A codec
        
        // When: Getting specifier with default and reconstructing
        int[] specifiers = CodecEncoding.getSpecifier(outerRunCodec, defaultCodec);
        byte[] headerBytes = extractHeaderBytes(specifiers);
        InputStream headerStream = new ByteArrayInputStream(headerBytes);
        RunCodec reconstructedCodec = (RunCodec) CodecEncoding.getCodec(specifiers[0], headerStream, defaultCodec);
        
        // Then: Should reconstruct correctly despite the default optimization
        assertEquals(outerRunCodec.getK(), reconstructedCodec.getK());
        assertEquals(outerRunCodec.getACodec(), reconstructedCodec.getACodec());
        
        RunCodec reconstructedInner = (RunCodec) reconstructedCodec.getBCodec();
        RunCodec originalInner = (RunCodec) outerRunCodec.getBCodec();
        assertRunCodecsEqual(originalInner, reconstructedInner);
    }

    // Helper methods

    /**
     * Extracts header bytes from specifier array (all elements except the first).
     */
    private byte[] extractHeaderBytes(int[] specifiers) {
        byte[] headerBytes = new byte[specifiers.length - 1];
        for (int i = 0; i < headerBytes.length; i++) {
            headerBytes[i] = (byte) specifiers[i + 1];
        }
        return headerBytes;
    }

    /**
     * Asserts that two RunCodecs are equivalent.
     */
    private void assertRunCodecsEqual(RunCodec expected, RunCodec actual) {
        assertEquals(expected.getK(), actual.getK(), "K values should match");
        assertEquals(expected.getACodec(), actual.getACodec(), "A codecs should match");
        assertEquals(expected.getBCodec(), actual.getBCodec(), "B codecs should match");
    }
}