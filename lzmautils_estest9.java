package org.apache.commons.compress.compressors.lzma;

import org.junit.Test;

/**
 * Tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Tests that calling isCompressedFileName with a null input throws a
     * NullPointerException, as the underlying utility expects a non-null file name.
     */
    @Test(expected = NullPointerException.class)
    public void isCompressedFileNameShouldThrowNullPointerExceptionForNullInput() {
        LZMAUtils.isCompressedFileName(null);
    }
}