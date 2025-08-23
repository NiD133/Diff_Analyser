package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.EOFException;

/**
 * Unit tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readInt() throws an EOFException when there are not enough
     * bytes remaining in the source to read a full integer (4 bytes).
     */
    @Test(expected = EOFException.class)
    public void readInt_shouldThrowEOFException_whenSourceHasInsufficientBytes() throws IOException {
        // Arrange: Create a data source with only one byte.
        // An integer requires four bytes, so this is insufficient.
        byte[] sourceBytes = new byte[1];
        RandomAccessFileOrArray randomAccess = new RandomAccessFileOrArray(sourceBytes);

        // Act: Attempt to read an integer from the source.
        // Assert: An EOFException is expected, as declared in the @Test annotation.
        randomAccess.readInt();
    }
}