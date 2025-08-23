package org.apache.commons.io;

import org.junit.Test;

/**
 * Unit tests for {@link RandomAccessFileMode}.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that {@link RandomAccessFileMode#io(String)} throws a NullPointerException
     * when the file name argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void testIoWithNullFileNameThrowsNullPointerException() {
        // The specific mode does not matter, as the null check on the file name
        // should precede any file operations.
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE;
        
        // This call is expected to throw a NullPointerException.
        mode.io(null);
    }
}