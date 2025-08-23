package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readFloatLE() throws an EOFException when there are not enough
     * bytes remaining in the source to read a full float (which requires 4 bytes).
     */
    @Test(expected = EOFException.class, timeout = 4000)
    public void readFloatLE_shouldThrowEOFException_whenSourceContainsInsufficientBytes() throws IOException {
        // Arrange: A float requires 4 bytes. We create a source with only 1 byte.
        byte[] insufficientBytes = new byte[1];
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(insufficientBytes);

        // Act: Attempt to read a little-endian float from the source.
        // This action is expected to throw an EOFException because the source is exhausted.
        randomAccess.readFloatLE();

        // Assert: The test passes if the expected EOFException is thrown,
        // which is handled declaratively by the @Test(expected=...) annotation.
    }
}