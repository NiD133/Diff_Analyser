package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * This test case verifies the behavior of the RandomAccessFileOrArray class
 * when read operations are attempted after the instance has been closed.
 */
// Note: The class name and inheritance are preserved from the original auto-generated test.
public class RandomAccessFileOrArray_ESTestTest61 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that calling readUnsignedShortLE() on a closed RandomAccessFileOrArray
     * instance throws a NullPointerException. This ensures that the object correctly
     * prevents operations on a released resource.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void readUnsignedShortLE_afterClose_throwsNullPointerException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray from a byte array and then close it.
        byte[] dummyData = new byte[16];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(dummyData);
        fileOrArray.close();

        // Act: Attempt to read from the closed instance.
        // This action is expected to throw a NullPointerException.
        fileOrArray.readUnsignedShortLE();

        // Assert: The test passes if the expected NullPointerException is thrown.
        // This is handled by the @Test(expected=...) annotation.
    }
}