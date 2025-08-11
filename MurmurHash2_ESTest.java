package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotEquals;
import static org.junit.Assert.assertThrows;

import java.nio.charset.StandardCharsets;

import org.junit.Test;

/**
 * Readable and maintainable tests for MurmurHash2.
 *
 * These tests focus on:
 * - Clear, canonical examples (empty input, simple strings).
 * - Overload consistency (String vs byte[] variants, substring behavior).
 * - Argument validation (nulls and out-of-bounds).
 * - A few stable, known results to act as anchors.
 *
 * Notes
 * - According to the Javadoc, String inputs are converted using UTF-8 since 1.14.
 * - Default seeds (documented):
 *   - 32-bit: 0x9747b28c
 *   - 64-bit: 0xe17a1465
 */
public class MurmurHash2Test {

    // Known stable results observed in Apache Commons Codec tests/builds.
    // These serve as "anchor" values to detect unintended changes.
    private static final int KNOWN_EMPTY_STRING_HASH32 = 275646681;
    private static final int KNOWN_STRINGUTILS_HASH32 = -1819289676;

    private static byte[] utf8(final String s) {
        return s.getBytes(StandardCharsets.UTF_8);
    }

    // --------------------------
    // 32-bit: Known values
    // --------------------------

    @Test
    public void hash32_emptyString_returnsKnownValue() {
        assertEquals(KNOWN_EMPTY_STRING_HASH32, MurmurHash2.hash32(""));
    }

    @Test
    public void hash32_knownString_returnsKnownValue() {
        assertEquals(KNOWN_STRINGUTILS_HASH32,
                MurmurHash2.hash32("org.apache.commons.codec.binary.StringUtils"));
    }

    // --------------------------
    // 32-bit: Overload consistency
    // --------------------------

    @Test
    public void hash32_stringVsBytes_sameContent_sameResult() {
        final String text = "hello, world";
        final byte[] bytes = utf8(text);

        assertEquals(MurmurHash2.hash32(text),
                     MurmurHash2.hash32(bytes, bytes.length));
    }

    @Test
    public void hash32_stringVsSubstring_fullLength_sameResult() {
        final String text = "abcdef";
        assertEquals(MurmurHash2.hash32(text),
                     MurmurHash2.hash32(text, 0, text.length()));
    }

    @Test
    public void hash32_substring_zeroLength_equalsEmptyStringHash() {
        final String text = "anything";
        assertEquals(KNOWN_EMPTY_STRING_HASH32, MurmurHash2.hash32(text, 2, 0));
    }

    @Test
    public void hash32_differentSeeds_produceDifferentResults() {
        final byte[] bytes = utf8("seed check");
        final int h1 = MurmurHash2.hash32(bytes, bytes.length, 1);
        final int h2 = MurmurHash2.hash32(bytes, bytes.length, 2);
        assertNotEquals(h1, h2);
    }

    // --------------------------
    // 32-bit: Argument validation
    // --------------------------

    @Test
    public void hash32_nullString_throwsNPE() {
        assertThrows(NullPointerException.class, () -> MurmurHash2.hash32((String) null));
    }

    @Test
    public void hash32_nullBytesWithLength_throwsNPE() {
        assertThrows(NullPointerException.class, () -> MurmurHash2.hash32((byte[]) null, 32));
    }

    @Test
    public void hash32_bytesLengthExceedsArray_throwsArrayIndexOutOfBounds() {
        final byte[] empty = new byte[0];
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> MurmurHash2.hash32(empty, 1834));
    }

    @Test
    public void hash32_bytes_withZeroLengthAndZeroSeed_returnsZero() {
        final byte[] any = new byte[5]; // content does not matter for length=0
        assertEquals(0, MurmurHash2.hash32(any, 0, 0));
    }

    @Test
    public void hash32_substring_outOfBounds_throwsStringIndexOutOfBounds() {
        assertThrows(StringIndexOutOfBoundsException.class,
                () -> MurmurHash2.hash32(": ", 12, 12));
    }

    // --------------------------
    // 64-bit: Overload consistency and properties
    // --------------------------

    @Test
    public void hash64_stringVsBytes_sameContent_sameResult() {
        final String text = "hello, world";
        final byte[] bytes = utf8(text);

        assertEquals(MurmurHash2.hash64(text),
                     MurmurHash2.hash64(bytes, bytes.length));
    }

    @Test
    public void hash64_stringVsSubstring_fullLength_sameResult() {
        final String text = "abcdef";
        assertEquals(MurmurHash2.hash64(text),
                     MurmurHash2.hash64(text, 0, text.length()));
    }

    @Test
    public void hash64_differentInputs_produceDifferentResults() {
        assertNotEquals(MurmurHash2.hash64("abc"),
                        MurmurHash2.hash64("abd"));
    }

    // --------------------------
    // 64-bit: Argument validation
    // --------------------------

    @Test
    public void hash64_nullString_throwsNPE() {
        assertThrows(NullPointerException.class, () -> MurmurHash2.hash64((String) null));
    }

    @Test
    public void hash64_nullBytesWithLength_throwsNPE() {
        assertThrows(NullPointerException.class, () -> MurmurHash2.hash64((byte[]) null, 10));
    }

    @Test
    public void hash64_bytesLengthExceedsArray_throwsArrayIndexOutOfBounds() {
        final byte[] empty = new byte[0];
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> MurmurHash2.hash64(empty, 2441));
    }

    @Test
    public void hash64_bytes_withZeroLengthAndZeroSeed_returnsZero() {
        final byte[] any = new byte[2]; // content does not matter for length=0
        assertEquals(0L, MurmurHash2.hash64(any, 0, 0));
    }
}