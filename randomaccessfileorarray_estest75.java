package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;

/**
 * This test verifies the behavior of the RandomAccessFileOrArray class after it has been closed.
 */
public class RandomAccessFileOrArray_ESTestTest75 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray after it has been closed
     * results in a NullPointerException. This is the expected behavior as closing the resource
     * releases its internal data source.
     */
    @Test(expected = NullPointerException.class, timeout = 4000)
    public void readLongLE_afterClose_throwsNullPointerException() throws IOException {
        // Arrange: Create a RandomAccessFileOrArray with some data and then close it.
        // The content of the data is irrelevant for this test. A long is 8 bytes, so we use an array of that size.
        byte[] anyData = new byte[8];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(anyData);
        fileOrArray.close();

        // Act & Assert: Attempt to read from the closed object.
        // The @Test(expected = NullPointerException.class) annotation handles the assertion.
        fileOrArray.readLongLE();
    }
}