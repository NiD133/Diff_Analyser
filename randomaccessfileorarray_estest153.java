package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

/**
 * This test class contains tests for the RandomAccessFileOrArray class.
 * This specific test focuses on the behavior of the readFully() method.
 */
public class RandomAccessFileOrArray_ESTestTest153 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that readFully() throws an EOFException when attempting to read more bytes
     * than are available in the underlying data source.
     */
    @Test(timeout = 4000, expected = EOFException.class)
    public void readFully_whenNotEnoughBytesRemain_throwsEOFException() throws IOException {
        // Arrange: Create a data source with 8 bytes.
        byte[] sourceData = new byte[8];
        RandomAccessFileOrArray accessor = new RandomAccessFileOrArray(sourceData);

        // Act: Consume the first 2 bytes, which leaves only 6 bytes remaining in the source.
        accessor.readShortLE();

        // Assert: Attempting to read a full 8 bytes into the buffer should fail because
        // only 6 are left. The method is expected to throw an EOFException.
        accessor.readFully(sourceData);
    }
}