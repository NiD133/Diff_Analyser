package org.apache.commons.io;

import org.junit.Test;
import static org.junit.Assert.assertEquals;
import java.io.File;
import java.io.FileNotFoundException;
import java.nio.file.OpenOption;
import java.nio.file.StandardOpenOption;
import org.evosuite.runtime.mock.java.io.MockFile;

// Note: Unused imports from the original file have been removed for clarity.
// The original test class structure is preserved.
public class RandomAccessFileMode_ESTestTest33 extends RandomAccessFileMode_ESTest_scaffolding {

    /**
     * Tests that RandomAccessFileMode.valueOf() correctly interprets the
     * StandardOpenOption.WRITE option as READ_WRITE mode.
     */
    @Test
    public void valueOfWithWriteOptionShouldReturnReadWriteMode() throws FileNotFoundException {
        // Arrange: Define the input options for creating the file mode.
        final OpenOption[] openOptions = { StandardOpenOption.WRITE };
        final File testFile = new MockFile("test-file.txt");

        // Act: Get the RandomAccessFileMode from the open options.
        final RandomAccessFileMode fileMode = RandomAccessFileMode.valueOf(openOptions);
        
        // Create a file instance using the determined mode.
        // The create() method returns an IORandomAccessFile, which has the getMode() method.
        final IORandomAccessFile randomAccessFile = (IORandomAccessFile) fileMode.create(testFile);

        // Assert: Verify that the correct mode was selected and applied.
        assertEquals("The file mode enum should be READ_WRITE", RandomAccessFileMode.READ_WRITE, fileMode);
        assertEquals("The underlying file mode string should be 'rw'", "rw", randomAccessFile.getMode());
    }
}