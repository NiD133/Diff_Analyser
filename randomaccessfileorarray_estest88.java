package com.itextpdf.text.pdf;

import org.junit.Test;
import java.io.EOFException;
import java.io.IOException;

/**
 * Contains tests for the {@link RandomAccessFileOrArray} class.
 */
public class RandomAccessFileOrArrayTest {

    /**
     * Verifies that attempting to read a float from a data source with insufficient
     * bytes throws an EOFException. A float requires 4 bytes, but the source
     * in this test only provides 2.
     */
    @Test(expected = EOFException.class)
    public void readFloat_withInsufficientBytes_throwsEOFException() throws IOException {
        // Arrange: Create a data source with only 2 bytes. This is not enough
        // to read a 4-byte float.
        byte[] insufficientData = new byte[2];
        RandomAccessFileOrArray fileOrArray = new RandomAccessFileOrArray(insufficientData);

        // Act & Assert: Attempt to read a float. This should fail and throw an
        // EOFException, which is verified by the @Test(expected=...) annotation.
        fileOrArray.readFloat();
    }
}