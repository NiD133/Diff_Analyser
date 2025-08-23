package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.*;
import java.io.IOException;

public class RandomAccessFileOrArray_ESTestTest40 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that readInt() correctly incorporates a pushed-back byte.
     * When a byte is pushed back using pushBack(), a subsequent call to readInt()
     * should use that byte as the first (most significant) byte of the resulting integer,
     * and the remaining three bytes should be read from the underlying data source.
     */
    @Test(timeout = 4000)
    public void readIntAfterPushBack_usesPushedBackByteAsFirstByte() throws IOException {
        // Arrange: Set up a data source of zeros and a reader.
        byte[] sourceData = new byte[5]; // An array of {0, 0, 0, 0, 0}
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        // Push back a byte. This byte will be the next one read, before the underlying source.
        byte pushedBackByte = 10; // 0x0A in hexadecimal
        reader.pushBack(pushedBackByte);

        // Act: Read a 4-byte integer.
        // The reader should consume the pushed-back byte (10) and the next 3 bytes from the source (0, 0, 0).
        // The resulting big-endian integer should be 0x0A000000.
        int actualInt = reader.readInt();

        // Assert: Verify the read value and the final position of the file pointer.
        // The expected integer is 10 * 2^24 = 167,772,160. Using hex makes the byte layout clear.
        int expectedInt = 0x0A000000;
        assertEquals("The integer should be formed by the pushed-back byte and three subsequent zeros.",
                expectedInt, actualInt);

        // The file pointer should have advanced by 3 positions in the sourceData array,
        // as only three bytes were read from it. The pushed-back byte doesn't affect the pointer.
        long expectedFilePointer = 3L;
        assertEquals("File pointer should advance by 3, reflecting reads from the source array only.",
                expectedFilePointer, reader.getFilePointer());
    }
}