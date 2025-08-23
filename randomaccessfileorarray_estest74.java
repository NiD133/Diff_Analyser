package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.IOException;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read from a RandomAccessFileOrArray instance after it
     * has been closed results in a NullPointerException. This confirms that the object
     * correctly invalidates its state and releases its resources upon closing.
     */
    @Test
    public void readShort_afterClose_throwsNullPointerException() {
        // Arrange: Create an instance from a byte array and then close it.
        byte[] sourceData = new byte[5];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(sourceData);
        try {
            fileOrArray.close();
        } catch (IOException e) {
            // The close() method might throw an IOException. If it fails during setup,
            // the test cannot proceed and should be marked as a failure.
            fail("Test setup failed: closing the RandomAccessFileOrArray instance threw an unexpected exception: " + e.getMessage());
        }

        // Act & Assert: Verify that a subsequent read operation throws a NullPointerException.
        assertThrows(NullPointerException.class, fileOrArray::readShort);
    }
}