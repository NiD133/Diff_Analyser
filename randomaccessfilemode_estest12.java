package org.apache.commons.io;

import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for the {@link RandomAccessFileMode#create(File)} method.
 */
public class RandomAccessFileModeTest {

    /**
     * The TemporaryFolder Rule allows creation of files and folders that are
     * guaranteed to be deleted when the test method finishes.
     */
    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * Tests that creating a file with READ_WRITE_SYNC_CONTENT mode
     * results in a RandomAccessFile opened with the "rwd" mode string.
     */
    @Test
    public void createWithReadWriteSyncContentModeShouldUseCorrectRwdModeString() throws IOException {
        // Arrange: Create a temporary file to be used by the test.
        final File testFile = temporaryFolder.newFile("test-file.txt");
        final RandomAccessFileMode mode = RandomAccessFileMode.READ_WRITE_SYNC_CONTENT;
        final String expectedModeString = "rwd";

        // Act & Assert: Create the RandomAccessFile and verify its internal mode.
        // Using a try-with-resources statement ensures the file is properly closed.
        try (RandomAccessFile raf = mode.create(testFile)) {
            // The create() method returns an IORandomAccessFile, which exposes getMode().
            // A cast is necessary to access this method.
            IORandomAccessFile ioraf = (IORandomAccessFile) raf;
            assertEquals(expectedModeString, ioraf.getMode());
        }
    }
}