package org.apache.commons.codec.digest;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

import org.junit.Test;

/**
 * Readable and intention-revealing tests for XXHash32.
 *
 * Key ideas:
 * - Use descriptive test names and comments.
 * - Favor realistic inputs (strings, small arrays) over magic numbers.
 * - Assert well-known constants only when they are stable (e.g., empty input).
 * - Check incremental behavior and API contracts (offset/length, reset, single-byte updates).
 */
public class XXHash32Test {

    // Known result of xxHash32 for empty input with seed 0.
    // See https://cyan4973.github.io/xxHash/ ("xxh32('', 0)")
    private static final long XXH32_EMPTY_SEED0 = 0x02CC5D05L; // 46947589L

    // Known result for empty input with seed 97 (taken from original tests).
    private static final int CUSTOM_SEED = 97;
    private static final long XXH32_EMPTY_SEED97 = 3659767818L;

    @Test
    public void emptyInput_withDefaultSeed_hasKnownHash() {
        XXHash32 h = new XXHash32(); // seed=0
        assertEquals(XXH32_EMPTY_SEED0, h.getValue());
    }

    @Test
    public void emptyInput_withCustomSeed_hasKnownHash() {
        XXHash32 h = new XXHash32(CUSTOM_SEED);
        assertEquals(XXH32_EMPTY_SEED97, h.getValue());
    }

    @Test
    public void update_zeroLength_isNoOp() {
        XXHash32 h = new XXHash32();
        byte[] any = "ignored".getBytes(StandardCharsets.US_ASCII);

        // Update with zero bytes must not change the state
        h.update(any, 0, 0);

        assertEquals(XXH32_EMPTY_SEED0, h.getValue());
    }

    @Test
    public void reset_restoresEmptyHash() {
        XXHash32 h = new XXHash32();
        h.update("abc".getBytes(StandardCharsets.US_ASCII), 0, 3);
        h.reset();
        assertEquals(XXH32_EMPTY_SEED0, h.getValue());
    }

    @Test
    public void singlePass_and_multiPart_updates_produceSameHash() {
        byte[] data = "The quick brown fox jumps over the lazy dog".getBytes(StandardCharsets.US_ASCII);

        // Single pass
        XXHash32 singlePass = new XXHash32();
        singlePass.update(data, 0, data.length);
        long expected = singlePass.getValue();

        // Multiple chunks crossing internal 16-byte block boundaries
        XXHash32 multi = new XXHash32();
        multi.update(data, 0, 7);
        multi.update(data, 7, 19);
        multi.update(data, 26, data.length - 26);
        long actual = multi.getValue();

        assertEquals(expected, actual);
    }

    @Test
    public void subArray_update_matches_hashOfCopiedSlice() {
        byte[] data = "abcdefghijklmnopqrstuvwxyz".getBytes(StandardCharsets.US_ASCII);
        int off = 3;
        int len = 17;

        // Reference by hashing a copied slice
        byte[] slice = Arrays.copyOfRange(data, off, off + len);
        XXHash32 ref = new XXHash32();
        ref.update(slice, 0, slice.length);

        // Hashing the same region via (off, len)
        XXHash32 viaOffsets = new XXHash32();
        viaOffsets.update(data, off, len);

        assertEquals(ref.getValue(), viaOffsets.getValue());
    }

    @Test
    public void update_int_usesOnlyLowOrderByte() {
        // Checksum.update(int) must use the low-order 8 bits only.
        XXHash32 a = new XXHash32();
        a.update(0x01);

        XXHash32 b = new XXHash32();
        b.update(0x101); // 0x101 & 0xFF == 0x01

        assertEquals(a.getValue(), b.getValue());
    }

    @Test
    public void mixing_updateInt_and_updateArray_matches_hashOfConcatenatedBytes() {
        // Build the byte sequence [2, 2, 0, 8] in two different ways and compare.
        byte[] bytes = new byte[] {2, 2, 0, 8};

        XXHash32 ref = new XXHash32();
        ref.update(bytes, 0, bytes.length);

        XXHash32 incremental = new XXHash32();
        incremental.update(2);
        incremental.update(2);
        incremental.update(0);
        incremental.update(8);

        assertEquals(ref.getValue(), incremental.getValue());
    }

    @Test
    public void update_withNullArray_throwsNullPointerException() {
        XXHash32 h = new XXHash32();
        try {
            h.update(null, 0, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    @Test
    public void update_withOutOfRangeOffsetOrLength_throwsIndexOutOfBounds() {
        XXHash32 h = new XXHash32();
        byte[] data = new byte[10];

        // off < 0
        expectIndexOutOfBounds(() -> h.update(data, -1, 1));

        // len < 0
        expectIndexOutOfBounds(() -> h.update(data, 0, -1));

        // off > data.length
        expectIndexOutOfBounds(() -> h.update(data, 11, 1));

        // off + len > data.length
        expectIndexOutOfBounds(() -> h.update(data, 5, 6));
    }

    // ----------------------------------------------------------------------
    // Utilities
    // ----------------------------------------------------------------------

    private interface ThrowingRunnable {
        void run();
    }

    private static void expectIndexOutOfBounds(ThrowingRunnable r) {
        try {
            r.run();
            fail("Expected an IndexOutOfBoundsException (or a subclass)");
        } catch (IndexOutOfBoundsException expected) {
            // ok (ArrayIndexOutOfBoundsException is also acceptable)
        }
    }
}