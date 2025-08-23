package org.apache.commons.compress.compressors.lzma;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

/**
 * Unit tests for the {@link LZMAUtils} class.
 */
public class LZMAUtilsTest {

    /**
     * Verifies that getUncompressedFileName() throws a NullPointerException
     * when the input filename is null.
     */
    @Test
    public void getUncompressedFileNameShouldThrowNullPointerExceptionForNullInput() {
        // The method is expected to throw a NullPointerException for null input,
        // as this is a standard contract for methods accepting String arguments.
        assertThrows(NullPointerException.class, () -> {
            LZMAUtils.getUncompressedFileName(null);
        });
    }
}