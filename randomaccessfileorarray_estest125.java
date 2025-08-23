package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertEquals;

/**
 * This class contains tests for the RandomAccessFileOrArray class.
 * The original test class name and scaffolding are preserved from the generated suite.
 */
public class RandomAccessFileOrArray_ESTestTest125 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that read() after a pushBack() correctly returns the pushed-back byte
     * as an unsigned integer, without advancing the file pointer.
     *
     * The read() method is expected to return an int in the range 0-255.
     */
    @Test
    public void readAfterPushBack_returnsPushedBackByteAsUnsignedInt_andDoesNotAdvancePointer() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray instance. The initial content is irrelevant
        // as we are testing the pushBack functionality.
        byte[] initialData = new byte[0];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(initialData);

        byte byteToPushBack = (byte) -69;
        // The read() method returns the byte as an unsigned int (0-255).
        // The bitwise AND with 0xFF converts the signed byte to its unsigned int equivalent.
        int expectedUnsignedValue = byteToPushBack & 0xFF; // This evaluates to 187
        long expectedFilePointer = 0L;

        // Act: Push a byte back into the stream and then read it.
        fileOrArray.pushBack(byteToPushBack);
        int actualReadValue = fileOrArray.read();
        long actualFilePointer = fileOrArray.getFilePointer();

        // Assert: Verify that the read value is correct and the pointer hasn't moved.
        assertEquals("The file pointer should not advance when reading a pushed-back byte.",
                expectedFilePointer, actualFilePointer);
        assertEquals("read() should return the pushed-back byte as an unsigned integer.",
                expectedUnsignedValue, actualReadValue);
    }
}