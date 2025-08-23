package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * This class contains tests for the RandomAccessFileOrArray class.
 * The original test class name and inheritance are preserved to maintain the original test suite's context.
 */
public class RandomAccessFileOrArray_ESTestTest126 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that calling the read() method on a RandomAccessFileOrArray instance
     * advances its internal file pointer by exactly one position.
     */
    @Test(timeout = 4000)
    public void read_whenCalledOnce_advancesFilePointerByOne() throws IOException {
        // Arrange: Create a reader from a byte array and verify its initial state.
        byte[] sourceData = new byte[2]; // The content doesn't matter, only that it's readable.
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        assertEquals("The initial file pointer should be at the beginning (position 0).", 0L, reader.getFilePointer());

        // Act: Read a single byte from the source.
        reader.read();

        // Assert: Confirm that the file pointer has moved forward by one.
        long expectedPosition = 1L;
        assertEquals("The file pointer should advance to position 1 after a single read.", expectedPosition, reader.getFilePointer());
    }
}