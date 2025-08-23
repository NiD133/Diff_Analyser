package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.file.Path;

import org.apache.commons.io.function.IOConsumer;
import org.evosuite.runtime.mock.java.io.MockFile;

/**
 * Tests for the {@link RandomAccessFileMode} enum.
 * This refactored test focuses on verifying the behavior of the accept() method.
 */
public class RandomAccessFileModeTest {

    /**
     * Tests that {@link RandomAccessFileMode#accept(Path, IOConsumer)} correctly
     * opens a file in write mode, allows a consumer to perform an action, and then
     * automatically closes the file resource.
     */
    @Test(timeout = 4000)
    public void acceptShouldWriteToFileAndCloseResource() throws IOException {
        // Arrange: Create a temporary file and a consumer that writes a specific byte.
        // Using a temporary file ensures the test is isolated and doesn't leave artifacts.
        File tempFile = MockFile.createTempFile("testAccept", ".tmp");
        tempFile.deleteOnExit(); // Ensure cleanup after the test run
        Path tempPath = tempFile.toPath();

        final RandomAccessFileMode writeMode = RandomAccessFileMode.READ_WRITE_SYNC_ALL;
        final int expectedByte = 42;

        // This consumer will be executed by the accept() method.
        final IOConsumer<RandomAccessFile> writerConsumer = raf -> raf.write(expectedByte);

        // Act: Use the accept() method to open the file, write to it, and close it.
        writeMode.accept(tempPath, writerConsumer);

        // Assert: Verify that the byte was successfully written to the file.
        // We read the file again to confirm its contents after the 'accept' operation is complete.
        try (RandomAccessFile reader = new RandomAccessFile(tempFile, "r")) {
            assertEquals("File should contain exactly one byte.", 1, reader.length());
            assertEquals("The byte read from the file should match the one that was written.",
                         expectedByte, reader.read());
        }
    }
}