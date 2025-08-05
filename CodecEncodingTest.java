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

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.stream.IntStream;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link CodecEncoding}, which handles the serialization and deserialization of Codec instances.
 */
class CodecEncodingTest {

    private static final int BHSD_CODEC_SPECIFIER = 116;
    private static final int MIN_RUN_CODEC_SPECIFIER = 117;
    private static final int MAX_RUN_CODEC_SPECIFIER = 140;
    private static final int MIN_POPULATION_CODEC_SPECIFIER = 141;
    private static final int MAX_POPULATION_CODEC_SPECIFIER = 188;

    @Test
    @DisplayName("getCodec with specifier 0 should return the default codec")
    void getCodecWithZeroSpecifierShouldReturnDefaultCodec() throws IOException, Pack200Exception {
        final Codec defaultCodec = new BHSDCodec(2, 16);
        final Codec result = CodecEncoding.getCodec(0, null, defaultCodec);
        assertEquals(defaultCodec, result);
    }

    @Nested
    @DisplayName("Canonical BHSD Codec Tests")
    class CanonicalCodecTest {

        static IntStream canonicalCodecSpecifiers() {
            // The Pack200 spec defines canonical codecs for specifiers 1 through 115.
            return IntStream.rangeClosed(1, 115);
        }

        @ParameterizedTest(name = "Specifier: {0}")
        @MethodSource("canonicalCodecSpecifiers")
        @DisplayName("getCodec should return the correct canonical codec for a given specifier")
        void getCodecShouldReturnCorrectCanonicalCodec(final int specifier) throws IOException, Pack200Exception {
            final Codec expected = CodecEncoding.getCanonicalCodec(specifier);
            final Codec actual = CodecEncoding.getCodec(specifier, null, null);
            assertEquals(expected, actual);
        }

        @ParameterizedTest(name = "Specifier: {0}")
        @MethodSource("canonicalCodecSpecifiers")
        @DisplayName("getSpecifier should return the correct specifier for a canonical codec")
        void getSpecifierShouldReturnCorrectSpecifierForCanonicalCodec(final int specifier) throws IOException, Pack200Exception {
            final Codec codec = CodecEncoding.getCanonicalCodec(specifier);
            final int[] specifierData = CodecEncoding.getSpecifier(codec, null);

            // Canonical codecs are encoded with a single integer specifier and no extra bytes.
            assertArrayEquals(new int[] { specifier }, specifierData);
        }
    }

    @Nested
    @DisplayName("Arbitrary (Non-Canonical) BHSD Codec Tests")
    class ArbitraryBHSDCodecTest {

        static Stream<Arguments> arbitraryBHSDCodecsAndBytes() {
            return Stream.of(
                Arguments.of(new BHSDCodec(1, 256), new byte[] { 0x00, (byte) 0xFF }),
                Arguments.of(new BHSDCodec(5, 128, 2, 1), new byte[] { 0x25, (byte) 0x7F }),
                Arguments.of(new BHSDCodec(2, 128, 1, 1), new byte[] { 0x0B, (byte) 0x7F })
            );
        }

        static Stream<BHSDCodec> arbitraryBHSDCodecs() {
            return Stream.of(
                new BHSDCodec(2, 125, 0, 1),
                new BHSDCodec(3, 125, 2, 1),
                new BHSDCodec(4, 125),
                new BHSDCodec(5, 125, 2, 0),
                new BHSDCodec(3, 5, 2, 1)
            );
        }

        @ParameterizedTest
        @MethodSource("arbitraryBHSDCodecsAndBytes")
        @DisplayName("getCodec should correctly decode arbitrary BHSD codecs from a byte stream")
        void getCodecShouldDecodeArbitraryBHSDCodec(final BHSDCodec expectedCodec, final byte[] encodedBytes) throws IOException, Pack200Exception {
            final InputStream inputStream = new ByteArrayInputStream(encodedBytes);
            final Codec decodedCodec = CodecEncoding.getCodec(BHSD_CODEC_SPECIFIER, inputStream, null);
            assertEquals(expectedCodec, decodedCodec);
        }

        @ParameterizedTest
        @MethodSource("arbitraryBHSDCodecs")
        @DisplayName("Arbitrary BHSD codecs should round-trip successfully")
        void arbitraryBHSDCodecShouldRoundTrip(final BHSDCodec codec) throws IOException, Pack200Exception {
            assertCodecRoundTripSucceeds(codec, null);
        }
    }

    @Nested
    @DisplayName("RunCodec Tests")
    class RunCodecTest {

        @Test
        @DisplayName("A simple RunCodec should round-trip successfully")
        void simpleRunCodecShouldRoundTrip() throws IOException, Pack200Exception {
            final RunCodec runCodec = new RunCodec(25, Codec.DELTA5, Codec.BYTE1);
            final RunCodec roundTripped = assertCodecRoundTripSucceeds(runCodec, null);

            assertEquals(runCodec.getK(), roundTripped.getK());
            assertEquals(runCodec.getACodec(), roundTripped.getACodec());
            assertEquals(runCodec.getBCodec(), roundTripped.getBCodec());
        }

        @Test
        @DisplayName("A RunCodec with a component matching the default codec should round-trip")
        void runCodecWithDefaultComponentShouldRoundTrip() throws IOException, Pack200Exception {
            final RunCodec runCodec = new RunCodec(4096, Codec.DELTA5, Codec.BYTE1);
            final Codec defaultCodec = Codec.DELTA5; // A-Codec matches the default
            final RunCodec roundTripped = assertCodecRoundTripSucceeds(runCodec, defaultCodec);

            assertEquals(runCodec.getK(), roundTripped.getK());
            assertEquals(runCodec.getACodec(), roundTripped.getACodec());
            assertEquals(runCodec.getBCodec(), roundTripped.getBCodec());
        }

        @Test
        @DisplayName("A nested RunCodec should round-trip successfully")
        void nestedRunCodecShouldRoundTrip() throws IOException, Pack200Exception {
            final RunCodec nestedCodec = new RunCodec(25, Codec.UDELTA5, Codec.DELTA5);
            final RunCodec runCodec = new RunCodec(64, Codec.SIGNED5, nestedCodec);
            final RunCodec roundTripped = assertCodecRoundTripSucceeds(runCodec, null);

            assertEquals(runCodec.getK(), roundTripped.getK());
            assertEquals(runCodec.getACodec(), roundTripped.getACodec());

            final RunCodec originalBCoDec = assertInstanceOf(RunCodec.class, runCodec.getBCodec());
            final RunCodec roundTrippedBCodec = assertInstanceOf(RunCodec.class, roundTripped.getBCodec());

            assertEquals(originalBCoDec.getK(), roundTrippedBCodec.getK());
            assertEquals(originalBCoDec.getACodec(), roundTrippedBCodec.getACodec());
            assertEquals(originalBCoDec.getBCodec(), roundTrippedBCodec.getBCodec());
        }
    }

    @Nested
    @DisplayName("PopulationCodec Tests")
    class PopulationCodecTest {

        @Test
        @DisplayName("A PopulationCodec should round-trip successfully")
        void populationCodecShouldRoundTrip() throws IOException, Pack200Exception {
            final PopulationCodec populationCodec = new PopulationCodec(Codec.BYTE1, Codec.CHAR3, Codec.UNSIGNED5);
            final PopulationCodec roundTripped = assertCodecRoundTripSucceeds(populationCodec, null);

            assertEquals(populationCodec.getFavouredCodec(), roundTripped.getFavouredCodec());
            assertEquals(populationCodec.getTokenCodec(), roundTripped.getTokenCodec());
            assertEquals(populationCodec.getUnfavouredCodec(), roundTripped.getUnfavouredCodec());
        }
    }

    /**
     * Asserts that a given Codec can be successfully encoded to a specifier and then decoded back to an equivalent
     * Codec.
     *
     * @param original The original codec to test.
     * @param defaultForBand The default codec for the band, which can affect encoding.
     * @return The codec after the round-trip, for further assertions.
     */
    private <T extends Codec> T assertCodecRoundTripSucceeds(final T original, final Codec defaultForBand)
        throws IOException, Pack200Exception {
        // 1. Encode the Codec into its specifier and byte data representation.
        final int[] specifierData = CodecEncoding.getSpecifier(original, defaultForBand);
        final int specifier = specifierData[0];

        // Assert that the specifier is in the expected range for the codec type.
        if (original instanceof RunCodec) {
            assertTrue(specifier >= MIN_RUN_CODEC_SPECIFIER && specifier <= MAX_RUN_CODEC_SPECIFIER,
                "RunCodec specifier out of range");
        } else if (original instanceof PopulationCodec) {
            assertTrue(specifier >= MIN_POPULATION_CODEC_SPECIFIER && specifier <= MAX_POPULATION_CODEC_SPECIFIER,
                "PopulationCodec specifier out of range");
        }

        // 2. Decode the specifier and byte data back into a new Codec instance.
        final InputStream inputStream = specifierToInputStream(specifierData);
        final Codec roundTripped = CodecEncoding.getCodec(specifier, inputStream, defaultForBand);

        // 3. Assert that the original and round-tripped codecs are equivalent.
        assertEquals(original, roundTripped);

        @SuppressWarnings("unchecked")
        final T result = (T) roundTripped;
        return result;
    }

    /**
     * Converts a specifier array (int[]) into an InputStream containing the byte data part of the specifier.
     * The first element of the array is the specifier itself and is ignored.
     */
    private InputStream specifierToInputStream(final int[] specifierData) {
        final byte[] bytes = new byte[specifierData.length - 1];
        for (int i = 0; i < bytes.length; i++) {
            bytes[i] = (byte) specifierData[i + 1];
        }
        return new ByteArrayInputStream(bytes);
    }
}