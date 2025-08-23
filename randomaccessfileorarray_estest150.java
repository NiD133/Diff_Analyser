package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that readBoolean() correctly processes a byte from the pushback buffer.
     * When a non-zero byte is pushed back, readBoolean() should return true, and crucially,
     * the underlying file pointer should not advance.
     */
    @Test
    public void readBooleanWithPushedBackByte_shouldReturnTrueAndNotAdvancePointer() throws IOException {
        // Arrange: Create a reader and push a non-zero byte into its buffer.
        // The initial content of the source data is irrelevant for this test.
        byte[] sourceData = new byte[10];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(sourceData);

        final byte nonZeroByteToPushBack = (byte) -63;
        reader.pushBack(nonZeroByteToPushBack);

        // Act: Read a boolean value, which should come from the pushback buffer.
        boolean result = reader.readBoolean();

        // Assert: Verify the result and the state of the reader.
        assertTrue("readBoolean should return true for a non-zero pushed-back byte.", result);
        assertEquals("Reading a pushed-back byte should not advance the file pointer.", 0L, reader.getFilePointer());
    }
}