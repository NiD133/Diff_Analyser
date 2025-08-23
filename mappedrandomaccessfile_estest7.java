package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;
import java.nio.channels.FileChannel;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

// The original test class name suggests it's part of a larger, generated suite.
// We keep the inheritance for compatibility with the existing test setup.
public class MappedRandomAccessFile_ESTestTest7 extends MappedRandomAccessFile_ESTest_scaffolding {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * Verifies that after a MappedRandomAccessFile is closed, its state can still be queried
     * without causing an error. It checks that getFilePointer() returns the initial position (0)
     * and that the channel returned by getChannel() is correctly marked as closed.
     */
    @Test(timeout = 4000)
    public void getStateAfterClose_shouldReturnZeroPointerAndClosedChannel() throws IOException {
        // Arrange: Create a temporary file and open it in read-write mode.
        File tempFile = temporaryFolder.newFile("testfile.dat");
        MappedRandomAccessFile file = new MappedRandomAccessFile(tempFile.getAbsolutePath(), "rw");

        // Act: Close the file immediately after creation.
        file.close();

        // Assert: Check the state of the file object after it has been closed.

        // The file pointer should remain at its initial position.
        assertEquals("File pointer should be 0 for a closed file.", 0L, file.getFilePointer());

        // The getChannel() method should return the underlying channel object,
        // which should now be in a closed state. This also implicitly verifies
        // that calling getChannel() on a closed file does not throw an exception.
        FileChannel channel = file.getChannel();
        assertNotNull("getChannel() should not return null on a closed file.", channel);
        assertFalse("Channel should be closed after the MappedRandomAccessFile is closed.", channel.isOpen());
    }
}