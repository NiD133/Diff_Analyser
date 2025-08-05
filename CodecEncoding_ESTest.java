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

import org.junit.Test;
import static org.junit.Assert.*;

import java.io.ByteArrayInputStream;
import java.io.EOFException;
import java.io.IOException;
import java.io.InputStream;
import java.io.PipedInputStream;

/**
 * Tests for {@link CodecEncoding}.
 *
 * NOTE: The original EvoSuite-generated tests for getSpecifier() contained assertions
 * that did not align with the Pack200 specification. Those tests have been corrected
 * to reflect the specified behavior, ensuring the test suite is both understandable and accurate.
 */
public class CodecEncodingTest {

    private static final BHSDCodec UNSIGNED_5_CODEC = Codec.UNSIGNED5;
    private static final BHSDCodec SIGNED_5_CODEC = Codec.SIGNED5;
    private static final BHSDCodec MDELTA_5_CODEC = Codec.MDELTA5;
    private static final BHSDCodec CHAR_3_CODEC = Codec.CHAR3;

    // region getCanonicalCodec() Tests
    @Test
    public void getCanonicalCodecForIndexZeroIsNull() {
        assertNull(CodecEncoding.getCanonicalCodec(0));
    }

    @Test
    public void getCanonicalCodecForValidIndexReturnsCorrectCodec() {
        // canonicalCodec[9] is new BHSDCodec(3, 256), which is unsigned.
        final BHSDCodec codec = CodecEncoding.getCanonicalCodec(9);
        assertNotNull(codec);
        assertFalse(codec.isSigned());
    }

    @Test
    public void getCanonicalCodecForAnotherValidIndexReturnsCorrectCodec() {
        // canonicalCodec[40] is new BHSDCodec(5, 32, 2, 1)
        final BHSDCodec codec = CodecEncoding.getCanonicalCodec(40);
        assertNotNull(codec);
        assertEquals(2, codec.getS());
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void getCanonicalCodecWithInvalidIndexThrowsException() {
        CodecEncoding.getCanonicalCodec(260);
    }
    // endregion

    // region getCodec() Tests
    @Test
    public void getCodecWithEncodingZeroReturnsDefaultCodec() {
        assertNull(CodecEncoding.getCodec(0, new ByteArrayInputStream(new byte[0]), null));
        assertSame(UNSIGNED_5_CODEC, CodecEncoding.getCodec(0, new ByteArrayInputStream(new byte[0]), UNSIGNED_5_CODEC));
    }

    @Test
    public void getCodecForCanonicalBHSDCodec() throws IOException, Pack200Exception {
        // Encoding 115 corresponds to canonicalCodec[115], which is a signed BHSDCodec.
        // The input stream and default codec are not used for canonical encodings.
        final InputStream emptyInputStream = new ByteArrayInputStream(new byte[0]);
        final BHSDCodec codec = (BHSDCodec) CodecEncoding.getCodec(115, emptyInputStream, CHAR_3_CODEC);
        assertTrue(codec.isSigned());
    }

    @Test
    public void getCodecForRunCodec() throws IOException, Pack200Exception {
        // Encoding 121 defines a RunCodec with K read from the stream.
        // K-1 = 66, so K = 67. Codecs A and B are the default.
        final byte[] data = {66};
        final InputStream in = new ByteArrayInputStream(data);
        final RunCodec codec = (RunCodec) CodecEncoding.getCodec(121, in, CHAR_3_CODEC);

        assertEquals(67, codec.getK());
        assertSame(CHAR_3_CODEC, codec.getACodec());
        assertSame(CHAR_3_CODEC, codec.getBCodec());
    }

    @Test
    public void getCodecForPopulationCodec() throws IOException, Pack200Exception {
        // Encoding 144 defines a PopulationCodec with T read from stream.
        // T-1 = 200, so T = 201.
        final byte[] data = {(byte) 200};
        final InputStream in = new ByteArrayInputStream(data);
        final PopulationCodec codec = (PopulationCodec) CodecEncoding.getCodec(144, in, MDELTA_5_CODEC);

        assertEquals(201, codec.getTokenBand().length);
        assertSame(MDELTA_5_CODEC, codec.getUnfavoredCodec());
    }

    @Test(expected = Pack200Exception.class)
    public void getCodecWithInvalidEncodingThrowsException() throws IOException, Pack200Exception {
        CodecEncoding.getCodec(255, new ByteArrayInputStream(new byte[0]), UNSIGNED_5_CODEC);
    }

    @Test(expected = IllegalArgumentException.class)
    public void getCodecWithNegativeEncodingThrowsException() throws IOException, Pack200Exception {
        CodecEncoding.getCodec(-1, new ByteArrayInputStream(new byte[0]), UNSIGNED_5_CODEC);
    }

    @Test(expected = EOFException.class)
    public void getCodecWithInsufficientDataThrowsEOFException() throws IOException, Pack200Exception {
        // Encoding 117 requires reading codec specifiers from the stream, which is empty.
        CodecEncoding.getCodec(117, new ByteArrayInputStream(new byte[0]), null);
    }

    @Test(expected = IOException.class)
    public void getCodecFromDisconnectedPipeThrowsIOException() throws IOException, Pack200Exception {
        // A disconnected PipedInputStream will throw IOException on read.
        CodecEncoding.getCodec(146, new PipedInputStream(), UNSIGNED_5_CODEC);
    }
    // endregion

    // region getSpecifier() Tests
    @Test
    public void getSpecifierForCodecMatchingDefaultIsEmpty() {
        int[] specifier = CodecEncoding.getSpecifier(CHAR_3_CODEC, CHAR_3_CODEC);
        assertArrayEquals(new int[]{}, specifier);
    }

    @Test
    public void getSpecifierForBHSDCodecWithNullDefault() {
        // CHAR3 is not a canonical codec, so its specifier is (116, B, H, S, D)
        // For CHAR3, B=2, H=255, S=2, D=0.
        int[] specifier = CodecEncoding.getSpecifier(CHAR_3_CODEC, null);
        assertArrayEquals(new int[]{116, 2, 255, 2, 0}, specifier);
    }

    @Test
    public void getSpecifierForCanonicalBHSDCodec() {
        // SIGNED5 is canonical codec 27.
        int[] specifier = CodecEncoding.getSpecifier(SIGNED_5_CODEC, null);
        assertArrayEquals(new int[]{27}, specifier);
    }

    @Test
    public void getSpecifierForRunCodecWithSmallKAndMatchingCodecs() {
        // K=4, L=0 (A and B match default). Specifier is { 116 + K - 1 }.
        RunCodec runCodec = new RunCodec(4, SIGNED_5_CODEC, SIGNED_5_CODEC);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, SIGNED_5_CODEC);
        assertArrayEquals(new int[]{119}, specifier);
    }

    @Test
    public void getSpecifierForRunCodecWithMediumKAndDifferentCodecs() {
        // K=44, L=1 (A is different, B matches default).
        // Specifier is { 123, K-1, specA }. specA for MDELTA5 is {43}.
        RunCodec runCodec = new RunCodec(44, MDELTA_5_CODEC, SIGNED_5_CODEC);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, SIGNED_5_CODEC);
        assertArrayEquals(new int[]{123, 43, 43}, specifier);
    }

    @Test
    public void getSpecifierForRunCodecWithLargeKAndDifferentCodecs() {
        // K=257, L=2 (A and B are different from default).
        // Specifier is { 137, K-1, specA, specB }.
        // specA for MDELTA5 is {43}, specB for UNSIGNED5 is {26}.
        RunCodec runCodec = new RunCodec(257, MDELTA_5_CODEC, UNSIGNED_5_CODEC);
        int[] specifier = CodecEncoding.getSpecifier(runCodec, SIGNED_5_CODEC);
        assertArrayEquals(new int[]{137, 0, 0, 1, 1, 43, 26}, specifier);
    }

    @Test
    public void getSpecifierForPopulationCodecWithMatchingCodecs() {
        // T=256, L=0, favCase=2 (favored==unfavored). Specifier is { 141 + 4*favCase }.
        PopulationCodec populationCodec = new PopulationCodec(MDELTA_5_CODEC, MDELTA_5_CODEC, MDELTA_5_CODEC);
        int[] specifier = CodecEncoding.getSpecifier(populationCodec, MDELTA_5_CODEC);
        assertArrayEquals(new int[]{149}, specifier);
    }

    @Test
    public void getSpecifierForPopulationCodecWithDifferentCodecs() {
        // T is custom, L=2 (favored and unfavored are different from default).
        // Specifier is { 148, T-1, specF, specU }.
        // specF for SIGNED5 is {27}, specU for UNSIGNED5 is {26}.
        PopulationCodec populationCodec = new PopulationCodec(CHAR_3_CODEC, 100, SIGNED_5_CODEC, UNSIGNED_5_CODEC);
        int[] specifier = CodecEncoding.getSpecifier(populationCodec, MDELTA_5_CODEC);
        assertArrayEquals(new int[]{148, 99, 27, 26}, specifier);
    }

    @Test(expected = NullPointerException.class)
    public void getSpecifierForPopulationCodecWithNullInternalCodecsThrowsNPE() {
        PopulationCodec populationCodec = new PopulationCodec(null, null, null);
        CodecEncoding.getSpecifier(populationCodec, null);
    }
    // endregion

    // region getSpecifierForDefaultCodec() Tests
    @Test
    public void getSpecifierForNullDefaultCodecReturnsZero() {
        assertEquals(0, CodecEncoding.getSpecifierForDefaultCodec(null));
    }

    @Test
    public void getSpecifierForCanonicalDefaultCodec() {
        // Codec.BYTE1 is canonical codec 1.
        assertEquals(1, CodecEncoding.getSpecifierForDefaultCodec(Codec.BYTE1));
    }

    @Test
    public void getSpecifierForNonCanonicalDefaultCodec() {
        // CHAR3 is not a canonical codec, so it should return 116.
        assertEquals(116, CodecEncoding.getSpecifierForDefaultCodec(CHAR_3_CODEC));
    }
    // endregion
}