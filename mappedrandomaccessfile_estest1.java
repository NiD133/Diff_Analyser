package com.itextpdf.text.pdf;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

import java.io.File;
import java.io.IOException;

import static org.junit.Assert.assertEquals;

/**
 * Contains focused tests for the {@link MappedRandomAccessFile} class.
 */
public class MappedRandomAccessFileTest {

    @Rule
    public TemporaryFolder temporaryFolder = new TemporaryFolder();

    /**
     * Verifies that calling read() with a negative length returns -1.
     * <p>
     * The standard behavior for I/O read methods with a negative length is often to throw
     * an IndexOutOfBoundsException. This test confirms a specific implementation
     * detail where -1 is returned instead, which typically indicates an end-of-file
     * or that no bytes were read. The test also includes seeking to a negative position
     * to cover this combination of edge cases found in the original test.
     * </p>
     */
    @Test
    public void read_withNegativeLength_shouldReturnMinusOne() throws IOException {
        // Arrange: Create an empty temporary file to be used by MappedRandomAccessFile.
        // This avoids creating files in the project directory and ensures cleanup.
        File emptyFile = temporaryFolder.newFile("empty.dat");

        try (MappedRandomAccessFile file = new MappedRandomAccessFile(emptyFile.getAbsolutePath(), "rw")) {
            assertEquals("Initial file pointer should be at the beginning", 0L, file.getFilePointer());

            // Act: Seek to an invalid negative position and then attempt to read with a negative length.
            // The key condition being tested is the negative length argument.
            file.seek(-1);
            int bytesRead = file.read(null, -1, -1);

            // Assert: The read operation should return -1, indicating no bytes were read.
            assertEquals("read() with a negative length should return -1", -1, bytesRead);
        }
    }
}