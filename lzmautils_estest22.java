package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;

/**
 * Tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that calling getUncompressedFileName with a null input
     * results in a NullPointerException, as the underlying utility
     * does not accept null file names.
     */
    @Test(expected = NullPointerException.class)
    public void getUncompressedFileNameShouldThrowExceptionForNullInput() {
        // This call is expected to throw a NullPointerException.
        // The @Test(expected=...) annotation handles the assertion.
        LZMAUtils.getUncompressedFileName(null);
    }
}