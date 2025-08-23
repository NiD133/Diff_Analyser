package org.apache.commons.io;

import org.junit.Test;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Path;

import static org.junit.Assert.fail; // This import is no longer needed but kept for context if other tests use it.

// The original test class structure is maintained.
public class RandomAccessFileMode_ESTestTest16 extends RandomAccessFileMode_ESTest_scaffolding {

    /**
     * Tests that attempting to create a RandomAccessFile in read-only mode ("r")
     * for a file that does not exist throws a FileNotFoundException. This is the
     * expected behavior, as read-only mode does not permit file creation.
     */
    @Test(expected = FileNotFoundException.class, timeout = 4000)
    public void createWithReadOnlyModeForNonExistentFileThrowsException() throws IOException {
        // Arrange: Set up the test conditions.
        final RandomAccessFileMode readOnlyMode = RandomAccessFileMode.valueOfMode("r");
        final File nonExistentFile = new File("this_file_should_not_exist.tmp");
        final Path nonExistentPath = nonExistentFile.toPath();

        // Act: Execute the method under test.
        // This call is expected to throw a FileNotFoundException because the file
        // does not exist and the mode is read-only.
        readOnlyMode.create(nonExistentPath);

        // Assert: The test passes if the expected exception is thrown.
        // This is handled by the @Test(expected) annotation. If no exception or a
        // different one is thrown, the test will fail.
    }
}