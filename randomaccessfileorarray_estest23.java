package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 * This specific test focuses on the behavior of the skip() method.
 */
// The original test class name and inheritance are preserved to maintain compatibility
// with the existing test suite structure.
public class RandomAccessFileOrArray_ESTestTest23 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Tests that calling skip() when the pointer is already at the end of the data
     * returns -1 and does not change the pointer's position.
     */
    @Test
    public void skip_whenAtEndOfFile_returnsNegativeOne() throws IOException {
        // Arrange: Set up a reader and advance its pointer to the end of the data.
        final int dataSize = 8;
        byte[] data = new byte[dataSize];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(data);

        // The readLine() call will consume all bytes because there are no newline
        // characters, effectively moving the pointer to the end of the data.
        reader.readLine();
        
        // Add a pre-condition check to ensure the setup is correct before acting.
        assertEquals("Pre-condition failed: pointer should be at the end before skipping.",
                     (long) dataSize, reader.getFilePointer());

        // Act: Attempt to skip bytes while the pointer is at the end of the file.
        long bytesSkipped = reader.skip(dataSize);

        // Assert: Verify that skip() returns -1 and the pointer remains at the end.
        // Note: This test asserts that skip() returns -1 at the end-of-file. This is
        // a specific behavior; a common alternative implementation would be to return 0.
        assertEquals("skip() should return -1 when called at the end of the file.",
                     -1L, bytesSkipped);
        assertEquals("The file pointer should remain at the end of the file.",
                     (long) dataSize, reader.getFilePointer());
    }
}