package org.apache.commons.codec.binary;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

/**
 * Contains improved, more understandable tests for the {@link BinaryCodec} class.
 *
 * Note: The original test was written for the Apache Commons Codec library's
 * {@code org.apache.commons.codec.binary.BinaryCodec}, not the spatial4j class
 * provided in the problem description. This improved test targets the correct
 * Apache Commons class.
 */
public class BinaryCodecImprovedTest {

    /**
     * Verifies that each encoding pass with {@code toAsciiBytes} expands the
     * data size by a factor of 8.
     *
     * This test clarifies the core behavior that was being implicitly tested in the
     * original, auto-generated test case. The original test repeatedly encoded
     * an array, causing exponential growth in its size, which likely led to an
     * OutOfMemoryError in a memory-constrained environment.
     *
     * Instead of relying on a brittle, environment-dependent crash, this test
     * verifies the deterministic property of the size expansion, which is the
     * root cause of the potential memory issue. This makes the test robust,
     * clear, and easy to maintain.
     */
    @Test
    public void toAsciiBytesShouldMultiplyArrayLengthByEightOnEachPass() {
        // ARRANGE: Define an initial small binary array.
        final int initialSize = 5;
        byte[] binaryData = new byte[initialSize];
        assertEquals("Initial data should have the expected size.", initialSize, binaryData.length);

        // ACT & ASSERT: After one encoding pass, the size should be 8 times larger.
        byte[] encodedOnce = BinaryCodec.toAsciiBytes(binaryData);
        final int expectedSizeAfterOnePass = initialSize * 8;
        assertEquals("After one encoding, size should be initialSize * 8.",
                expectedSizeAfterOnePass, encodedOnce.length);

        // ACT & ASSERT: After a second encoding pass, the size should again be 8 times larger.
        byte[] encodedTwice = BinaryCodec.toAsciiBytes(encodedOnce);
        final int expectedSizeAfterTwoPasses = expectedSizeAfterOnePass * 8;
        assertEquals("After two encodings, size should be initialSize * 8 * 8.",
                expectedSizeAfterTwoPasses, encodedTwice.length);
    }
}