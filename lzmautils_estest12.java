package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;

/**
 * Unit tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that calling getCompressedFileName with a null input throws a
     * NullPointerException, as the method contract requires a valid filename.
     */
    @Test(expected = NullPointerException.class)
    public void getCompressedFileNameShouldThrowExceptionForNullInput() {
        // The getCompressedFileName method is expected to delegate to a utility
        // that does not accept nulls. This test verifies that a
        // NullPointerException is thrown as expected.
        LZMAUtils.getCompressedFileName(null);
    }
}