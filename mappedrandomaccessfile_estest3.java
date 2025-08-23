package com.itextpdf.text.pdf;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains tests for the MappedRandomAccessFile class, focusing on read and seek operations.
 */
public class MappedRandomAccessFileTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    private MappedRandomAccessFile mraf;
    private File emptyFile;

    @Before
    public void setUp() throws IOException {
        // Arrange: Create a new empty file before each test.
        emptyFile = tempFolder.newFile("empty.dat");
    }

    @After
    public void tearDown() throws IOException {
        // Clean up: Ensure the file resource is closed after each test.
        if (mraf != null) {
            mraf.close();
        }
    }

    /**
     * Verifies that seeking beyond the end of an empty file and then attempting to read
     * returns -1, the standard indicator for End-Of-File (EOF).
     */
    @Test
    public void readShouldReturnEOFWhenSeekingPastEndOfFile() throws IOException {
        // Arrange
        mraf = new MappedRandomAccessFile(emptyFile.getAbsolutePath(), "rw");
        final long positionPastEnd = 100L; // An arbitrary position beyond the file's end (length is 0).
        final int EOF = -1;

        // Act: Seek to a position that is guaranteed to be past the end of the empty file.
        mraf.seek(positionPastEnd);

        // Assert: The file pointer should now be at the new position.
        assertEquals("File pointer should be updated to the seeked position.",
                positionPastEnd, mraf.getFilePointer());

        // Act: Attempt to read a byte from this position.
        int readResult = mraf.read();

        // Assert: The read operation should return EOF.
        assertEquals("Reading past the end of the file should return EOF (-1).",
                EOF, readResult);
    }
}