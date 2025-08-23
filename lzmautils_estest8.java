package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;

/**
 * Unit tests for the LZMAUtils class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that isCompressedFilename throws a NullPointerException when
     * the input filename is null. This behavior is delegated to an
     * underlying utility, and this test ensures that nulls are not
     * permitted.
     */
    @Test(expected = NullPointerException.class)
    public void isCompressedFilenameShouldThrowNullPointerExceptionForNullInput() {
        // The isCompressedFilename method is deprecated, but we still test its contract.
        // The new isCompressedFileName method delegates to the same underlying utility,
        // so its behavior with null input is expected to be identical.
        LZMAUtils.isCompressedFilename(null);
    }
}