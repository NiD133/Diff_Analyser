package org.apache.commons.io;

import static org.junit.Assert.assertThrows;

import java.nio.file.Path;
import java.nio.file.Paths;
import org.apache.commons.io.function.IOConsumer;
import org.junit.Test;

/**
 * Tests for {@link RandomAccessFileMode}.
 * This class contains the refactored test case.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that {@link RandomAccessFileMode#accept(Path, IOConsumer)} throws a
     * {@link NullPointerException} when the provided consumer is null.
     */
    @Test
    public void acceptShouldThrowNullPointerExceptionWhenConsumerIsNull() {
        // Arrange
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        // The path can point to a non-existent file, as the null check for the
        // consumer should happen before any file I/O.
        final Path path = Paths.get("test-file.txt");

        // Act & Assert
        // The call is expected to throw a NullPointerException because the consumer is null.
        assertThrows(NullPointerException.class, () -> mode.accept(path, null));
    }
}