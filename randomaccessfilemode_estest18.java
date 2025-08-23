package org.apache.commons.io;

import java.io.File;
import org.junit.Test;

/**
 * Tests for the {@link RandomAccessFileMode} enum.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that calling create() with a null File object throws a NullPointerException.
     */
    @Test(expected = NullPointerException.class)
    public void createWithNullFileShouldThrowNullPointerException() {
        // Arrange: Get an instance of the enum. The specific mode does not matter for this test.
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_ONLY;

        // Act: Attempt to create a RandomAccessFile with a null File argument.
        // Assert: The test framework will assert that a NullPointerException is thrown.
        mode.create((File) null);
    }
}