package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertThrows;

/**
 * Tests the behavior of {@link RandomAccessFileOrArray} after its resources have been closed.
 */
public class RandomAccessFileOrArray_ESTestTest72 extends RandomAccessFileOrArray_ESTest_scaffolding {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray instance after
     * it has been closed results in a NullPointerException. This ensures that the
     * object correctly invalidates its internal state upon closure.
     */
    @Test
    public void readShortLE_onClosedInstance_throwsNullPointerException() throws IOException {
        // Arrange: Create an instance backed by a byte array and immediately close it
        // to set up the "closed" state for the test.
        byte[] dummyData = new byte[4]; // Content and size are not relevant for this test.
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(dummyData);
        fileOrArray.close();

        // Act & Assert: Verify that calling readShortLE() on the closed instance
        // throws a NullPointerException.
        assertThrows(NullPointerException.class, fileOrArray::readShortLE);
    }
}