package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.EOFException;
import java.io.IOException;

/**
 * This class contains tests for the RandomAccessFileOrArray class.
 * This particular test was improved for clarity from an auto-generated version.
 */
public class RandomAccessFileOrArray_ESTestTest49 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that an EOFException is thrown when attempting to read beyond the end of the data source.
     * The test first positions the read pointer exactly at the end of the source data through a series
     * of read and skip operations before attempting the final, failing read.
     *
     * @throws IOException if an I/O error occurs during the setup phase.
     */
    @Test(timeout = 4000, expected = EOFException.class)
    public void readShortLE_whenAtEndOfData_throwsEOFException() throws IOException {
        // Arrange: Create a 9-byte data source and a reader. Then, consume all 9 bytes
        // to position the internal pointer at the very end.
        byte[] sourceData = new byte[9];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        // The sequence below is designed to consume exactly 9 bytes of data.
        // 1. A pushed-back byte is consumed first, followed by 7 bytes from the array.
        reader.pushBack((byte) -63);
        reader.readDouble(); // Consumes 8 bytes total (1 pushed-back + 7 from array). Pointer is at index 7.

        // 2. Skip one more byte from the array.
        reader.skipBytes(1); // Pointer is now at index 8.

        // 3. Read the final byte from the array.
        reader.readBoolean(); // Consumes 1 byte. Pointer is now at index 9, which is the end of the data.

        // Act: Attempt to read a short (2 bytes) when no bytes are left.
        // This action is expected to throw an EOFException.
        reader.readShortLE();

        // Assert: The 'expected = EOFException.class' annotation on the @Test
        // handles the assertion, failing the test if the expected exception is not thrown.
    }
}