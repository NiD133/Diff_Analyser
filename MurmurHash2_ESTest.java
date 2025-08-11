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
package org.apache.commons.codec.digest;

import org.junit.Test;
import static org.junit.Assert.*;

/**
 * Tests for the {@link MurmurHash2} class.
 *
 * <p>The expected hash values in this test suite are based on the output of the
 * original implementation and are preserved to ensure consistent behavior.</p>
 */
public class MurmurHash2Test {

    // Known hash values for an empty input with default seeds.
    // This is the result of finalizing the default seed value.
    private static final int EMPTY_INPUT_HASH32 = 275646681;
    private static final long EMPTY_INPUT_HASH64 = -7207201254813729732L;

    // --- 32-bit Hash Tests ---

    @Test
    public void testHash32_String_shouldReturnCorrectHashForNonEmptyInput() {
        final String input = "org.apache.commons.codec.binary.StringUtils";
        final int expectedHash = -1819289676;
        assertEquals(expectedHash, MurmurHash2.hash32(input));
    }

    @Test
    public void testHash32_String_shouldReturnKnownHashForEmptyString() {
        assertEquals(EMPTY_INPUT_HASH32, MurmurHash2.hash32(""));
    }

    @Test(expected = NullPointerException.class)
    public void testHash32_String_shouldThrowExceptionForNullInput() {
        MurmurHash2.hash32((String) null);
    }

    @Test
    public void testHash32_Substring_shouldReturnCorrectHash() {
        final String input = "9chG_Yo[`m";
        final int expectedHash = -1877468854; // Hash of "c"
        assertEquals(expectedHash, MurmurHash2.hash32(input, 1, 1));
    }

    @Test
    public void testHash32_Substring_shouldReturnKnownHashForZeroLength() {
        assertEquals(EMPTY_INPUT_HASH32, MurmurHash2.hash32("any-string", 4, 0));
    }

    @Test(expected = NullPointerException.class)
    public void testHash32_Substring_shouldThrowExceptionForNullInput() {
        MurmurHash2.hash32(null, 1, 1);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testHash32_Substring_shouldThrowExceptionForInvalidRange() {
        MurmurHash2.hash32("short", 0, 10);
    }

    @Test
    public void testHash32_ByteArray_shouldReturnZeroHashForZeroedInput() {
        final byte[] input = new byte[6];
        // A non-zero length with a zero-byte array and default seed results in a zero hash.
        assertEquals(0, MurmurHash2.hash32(input, input.length));
    }

    @Test
    public void testHash32_ByteArray_shouldReturnKnownHashForZeroLength() {
        assertEquals(EMPTY_INPUT_HASH32, MurmurHash2.hash32(new byte[5], 0));
    }

    @Test
    public void testHash32_ByteArray_shouldReturnKnownHashForNullInputAndZeroLength() {
        // The implementation treats a null array with zero length as an empty input.
        assertEquals(EMPTY_INPUT_HASH32, MurmurHash2.hash32(null, 0));
    }

    @Test(expected = NullPointerException.class)
    public void testHash32_ByteArray_shouldThrowExceptionForNullInputAndNonZeroLength() {
        MurmurHash2.hash32(null, 10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHash32_ByteArray_shouldThrowExceptionForLengthGreaterThanArraySize() {
        MurmurHash2.hash32(new byte[5], 10);
    }

    @Test
    public void testHash32_ByteArrayWithSeed_shouldReturnCorrectHash() {
        final byte[] input = new byte[6];
        final int length = 5;
        final int seed = 615;
        final int expectedHash = 1161250932;
        assertEquals(expectedHash, MurmurHash2.hash32(input, length, seed));
    }

    @Test
    public void testHash32_ByteArrayWithSeed_shouldReturnZeroForZeroLengthAndZeroSeed() {
        assertEquals(0, MurmurHash2.hash32(new byte[1], 0, 0));
    }

    @Test
    public void testHash32_ByteArrayWithSeed_shouldUseNegativeLengthInCalculation() {
        final byte[] input = new byte[2];
        final int seed = 0;
        final int length = -1564; // Negative length is used in the seed calculation.
        final int expectedHash = 1307949917;
        // This test documents a quirky behavior where the length parameter, even if
        // negative, contributes to the initial hash value.
        assertEquals(expectedHash, MurmurHash2.hash32(input, length, seed));
    }

    @Test(expected = NullPointerException.class)
    public void testHash32_ByteArrayWithSeed_shouldThrowExceptionForNullInput() {
        MurmurHash2.hash32(null, 1, 1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHash32_ByteArrayWithSeed_shouldThrowExceptionForOutOfBoundsRead() {
        // A negative length can cause an out-of-bounds read if (length & 3) > 0.
        // For length = -1, (length & 3) is 3, so it attempts to read 3 bytes.
        MurmurHash2.hash32(new byte[2], -1, 123);
    }

    // --- 64-bit Hash Tests ---

    @Test
    public void testHash64_String_shouldReturnCorrectHashForNonEmptyInput() {
        final String input = "q%DCbQXCHT4'G\"^L";
        final long expectedHash = 3105811143660689330L;
        assertEquals(expectedHash, MurmurHash2.hash64(input));
    }

    @Test
    public void testHash64_String_shouldReturnKnownHashForEmptyString() {
        assertEquals(EMPTY_INPUT_HASH64, MurmurHash2.hash64(""));
    }

    @Test(expected = NullPointerException.class)
    public void testHash64_String_shouldThrowExceptionForNullInput() {
        MurmurHash2.hash64((String) null);
    }

    @Test
    public void testHash64_Substring_shouldReturnCorrectHash() {
        final String input = "ylLM~55";
        final long expectedHash = 4591197677584300775L; // Hash of "l"
        assertEquals(expectedHash, MurmurHash2.hash64(input, 1, 1));
    }

    @Test
    public void testHash64_Substring_shouldReturnKnownHashForZeroLength() {
        assertEquals(EMPTY_INPUT_HASH64, MurmurHash2.hash64("any-string", 4, 0));
    }

    @Test(expected = NullPointerException.class)
    public void testHash64_Substring_shouldThrowExceptionForNullInput() {
        MurmurHash2.hash64(null, 1, 1);
    }

    @Test(expected = StringIndexOutOfBoundsException.class)
    public void testHash64_Substring_shouldThrowExceptionForInvalidRange() {
        MurmurHash2.hash64("short", -1, 10);
    }

    @Test
    public void testHash64_ByteArray_shouldReturnCorrectHash() {
        final byte[] input = new byte[5];
        input[0] = (byte) 24;
        final long expectedHash = 24027485454243747L;
        assertEquals(expectedHash, MurmurHash2.hash64(input, 1));
    }

    @Test
    public void testHash64_ByteArray_shouldReturnKnownHashForZeroLength() {
        assertEquals(EMPTY_INPUT_HASH64, MurmurHash2.hash64(new byte[5], 0));
    }

    @Test
    public void testHash64_ByteArray_shouldReturnKnownHashForNullInputAndZeroLength() {
        // The implementation treats a null array with zero length as an empty input.
        assertEquals(EMPTY_INPUT_HASH64, MurmurHash2.hash64(null, 0));
    }

    @Test(expected = NullPointerException.class)
    public void testHash64_ByteArray_shouldThrowExceptionForNullInputAndNonZeroLength() {
        MurmurHash2.hash64(null, 10);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHash64_ByteArray_shouldThrowExceptionForLengthGreaterThanArraySize() {
        MurmurHash2.hash64(new byte[5], 10);
    }

    @Test
    public void testHash64_ByteArrayWithSeed_shouldReturnCorrectHash() {
        final byte[] input = new byte[5];
        final int length = 1;
        final int seed = 1;
        final long expectedHash = -5720937396023583481L;
        assertEquals(expectedHash, MurmurHash2.hash64(input, length, seed));
    }

    @Test
    public void testHash64_ByteArrayWithSeed_shouldReturnZeroForZeroLengthAndZeroSeed() {
        assertEquals(0L, MurmurHash2.hash64(new byte[2], 0, 0));
    }

    @Test
    public void testHash64_ByteArrayWithSeed_shouldReturnFinalizedSeedForZeroLength() {
        final byte[] input = new byte[6];
        final int seed = -66;
        final int length = 0;
        final long expectedHash = 2692789288766115115L;
        // For zero-length data, the hash is the result of the finalization mix on the seed.
        assertEquals(expectedHash, MurmurHash2.hash64(input, length, seed));
    }

    @Test(expected = NullPointerException.class)
    public void testHash64_ByteArrayWithSeed_shouldThrowExceptionForNullInput() {
        MurmurHash2.hash64(null, 1, 1);
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void testHash64_ByteArrayWithSeed_shouldThrowExceptionForOutOfBoundsRead() {
        // A negative length can cause an out-of-bounds read if (length & 7) > 0.
        // For length = -1, (length & 7) is 7, so it attempts to read 7 bytes.
        MurmurHash2.hash64(new byte[6], -1, 123);
    }
}