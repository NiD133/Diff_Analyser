package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.assertEquals;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import org.evosuite.runtime.mock.java.io.MockFile;

// The test class structure and scaffolding from the original are preserved.
public class ArArchiveOutputStream_ESTestTest8 extends ArArchiveOutputStream_ESTest_scaffolding {

    /**
     * Tests that createArchiveEntry correctly determines the entry's size
     * from the source file's length.
     */
    @Test
    public void createArchiveEntryFromFileSetsLengthCorrectly() throws IOException {
        // Arrange: Create a temporary file with a known size.
        File testFile = MockFile.createTempFile("test-file", ".txt");
        String fileContent = "12345"; // This content is exactly 5 bytes long.

        try (PrintWriter writer = new PrintWriter(testFile)) {
            writer.print(fileContent);
        }

        // The ArArchiveOutputStream is only used as a factory for the entry in this test,
        // so the underlying output stream can be null.
        ArArchiveOutputStream arOut = new ArArchiveOutputStream(null);
        String entryName = "archive-entry.txt";

        // Act: Create an archive entry from the file.
        ArArchiveEntry entry = arOut.createArchiveEntry(testFile, entryName);

        // Assert: Verify the entry's length matches the file's content length.
        long expectedSize = fileContent.length();
        assertEquals("The archive entry length should match the source file size.",
                expectedSize, entry.getLength());
    }
}