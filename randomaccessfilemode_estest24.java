package org.apache.commons.io;

import org.junit.Test;
import java.nio.file.Path;

/**
 * Tests for the {@link RandomAccessFileMode} enum, focusing on exception handling.
 */
public class RandomAccessFileModeTest {

    /**
     * Verifies that calling the create(Path) method with a null Path argument
     * results in a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void createWithNullPathShouldThrowNullPointerException() {
        // Arrange: Select any mode, as the null-check behavior should be consistent across all modes.
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        final Path nullPath = null;

        // Act: Call the method with the null argument.
        // The @Test annotation's 'expected' parameter handles the assertion
        // that a NullPointerException is thrown.
        mode.create(nullPath);
    }
}