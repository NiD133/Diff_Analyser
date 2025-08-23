package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;

/**
 * Unit tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that the matches() method throws an ArrayIndexOutOfBoundsException
     * when the provided byte array is shorter than the length to be checked.
     */
    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void matchesShouldThrowExceptionWhenLengthIsGreaterThanArraySize() {
        // The LZMA header signature is 3 bytes long.
        final int requiredLength = 3;
        byte[] signatureWithInsufficientData = new byte[0];

        // This call is expected to throw an exception because we are asking
        // the method to read 3 bytes from an empty array.
        LZMAUtils.matches(signatureWithInsufficientData, requiredLength);
    }
}