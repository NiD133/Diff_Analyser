package com.itextpdf.text.pdf;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
// The original test extended a scaffolding class, which is retained here
// as its contents and purpose are external to this specific test.
public class RandomAccessFileOrArrayTest extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that reading a little-endian integer advances the file pointer
     * by the size of an integer (4 bytes).
     */
    @Test
    public void readIntLE_shouldAdvanceFilePointerByIntegerSize() throws IOException {
        // Arrange: Set up a data source and the reader.
        // The byte array's content is irrelevant; we only need to ensure it's large enough
        // to read an integer from.
        byte[] inputData = new byte[8];
        RandomAccessFileOrArray reader = new RandomAccessFileOrArray(inputData);

        // Act: Perform the operation under test.
        reader.readIntLE();

        // Assert: Verify the file pointer has moved to the correct position.
        long newFilePointer = reader.getFilePointer();
        long expectedPosition = Integer.BYTES; // An integer is 4 bytes.

        assertEquals("The file pointer should advance by the size of an integer after the read operation.",
                expectedPosition, newFilePointer);
    }
}