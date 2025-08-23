package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;
import org.apache.commons.io.function.IOConsumer;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.testdata.FileSystemHandling;

// The following imports are retained as they are part of the test class's original runner configuration.
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.junit.runner.RunWith;

public class RandomAccessFileMode_ESTestTest22 extends RandomAccessFileMode_ESTest_scaffolding {

    /**
     * Tests that {@link RandomAccessFileMode#accept(Path, IOConsumer)}
     * correctly propagates an {@link IOException} when the underlying file system
     * fails to open a file.
     */
    @Test(timeout = 4000)
    public void acceptShouldPropagateIOExceptionWhenFileSystemFails() {
        // Arrange: Configure the mock file system to throw an IOException on any file operation.
        // This simulates a scenario where the file cannot be accessed due to I/O errors.
        FileSystemHandling.shouldAllThrowIOExceptions();

        final Path targetPath = new MockFile("test-file.txt").toPath();
        final IOConsumer<RandomAccessFile> noopConsumer = IOConsumer.noop();
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;

        // Act & Assert
        try {
            mode.accept(targetPath, noopConsumer);
            fail("Expected an IOException to be thrown because the file system is mocked to fail.");
        } catch (final IOException e) {
            // Verify that the expected exception from the mock file system was caught.
            final String expectedMessage = "Simulated IOException";
            assertEquals("The exception message should match the one from the mock file system.",
                expectedMessage, e.getMessage());
        }
    }
}