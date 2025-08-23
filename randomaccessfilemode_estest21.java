package org.apache.commons.io;

import static org.junit.Assert.assertThrows;

import java.io.RandomAccessFile;
import java.nio.file.Path;
import org.apache.commons.io.function.IOConsumer;
import org.junit.Test;

/**
 * Contains tests for the {@link RandomAccessFileMode} enum.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that {@link RandomAccessFileMode#accept(Path, IOConsumer)} throws
     * a NullPointerException when the provided path is null.
     */
    @Test
    public void acceptWithNullPathShouldThrowNullPointerException() {
        // Arrange: Set up the test objects and inputs.
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        final IOConsumer<RandomAccessFile> noopConsumer = IOConsumer.noop();
        final Path nullPath = null;

        // Act & Assert: Verify that calling the method with a null path throws the expected exception.
        assertThrows(NullPointerException.class, () -> {
            mode.accept(nullPath, noopConsumer);
        });
    }
}