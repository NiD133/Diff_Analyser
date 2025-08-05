package org.apache.commons.compress.archivers.ar;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.concurrent.TimeUnit;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Understandable and maintainable tests for {@link ArArchiveOutputStream}.
 */
public class ArArchiveOutputStreamTest {

    @Rule
    public final TemporaryFolder tempFolder = new TemporaryFolder();

    private ByteArrayOutputStream outputBuffer;
    private ArArchiveOutputStream arOut;

    @Before
    public void setUp() {
        outputBuffer = new ByteArrayOutputStream();
        arOut = new ArArchiveOutputStream(outputBuffer);
    }

    @Test
    public void putArchiveEntryWritesMagicHeaderAndEntryHeader() throws IOException {
        // Arrange
        ArArchiveEntry entry = new ArArchiveEntry("test_entry.txt", 10);

        // Act
        arOut.putArchiveEntry(entry);

        // Assert
        // The total size should be the AR magic header plus the entry header.
        // AR magic: "!<arch>\n" (8 bytes)
        // Entry header: 60 bytes
        final int expectedSize = 8 + 60;
        assertEquals(expectedSize, arOut.getBytesWritten());
        assertTrue(outputBuffer.toString(StandardCharsets.US_ASCII).startsWith("!<arch>\n"));
    }

    @Test
    public void writeWithContentAndPadding() throws IOException {
        // Arrange
        byte[] content = "odd_length".getBytes(StandardCharsets.US_ASCII); // 10 bytes (even)
        ArArchiveEntry evenEntry = new ArArchiveEntry("even.txt", content.length);

        byte[] content2 = "odd".getBytes(StandardCharsets.US_ASCII); // 3 bytes (odd)
        ArArchiveEntry oddEntry = new ArArchiveEntry("odd.txt", content2.length);

        // Act & Assert for even length entry (no padding)
        arOut.putArchiveEntry(evenEntry);
        arOut.write(content);
        arOut.closeArchiveEntry();
        long expectedSize = 8 + 60 + content.length;
        assertEquals("Even length content should not be padded", expectedSize, arOut.getBytesWritten());

        // Act & Assert for odd length entry (1 byte padding)
        arOut.putArchiveEntry(oddEntry);
        arOut.write(content2);
        arOut.closeArchiveEntry();
        expectedSize += 60 + content2.length + 1; // +1 for padding
        assertEquals("Odd length content should be padded with one byte", expectedSize, arOut.getBytesWritten());
    }

    @Test
    public void putArchiveEntryWithLongNameAndDefaultModeThrowsException() {
        // Arrange
        // The default long file mode is LONGFILE_ERROR, which rejects names > 16 chars.
        ArArchiveEntry entry = new ArArchiveEntry("this-filename-is-too-long.txt", 0);

        // Act & Assert
        IOException e = assertThrows(IOException.class, () -> arOut.putArchiveEntry(entry));
        assertTrue(e.getMessage().startsWith("File name too long, > 16 chars:"));
    }

    @Test
    public void putArchiveEntryWithBsdLongFileModeHandlesLongName() throws IOException {
        // Arrange
        arOut.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
        String longName = "this-is-a-very-long-filename-for-testing.txt"; // 45 chars
        ArArchiveEntry entry = new ArArchiveEntry(longName, 0);

        // Act
        arOut.putArchiveEntry(entry);
        arOut.closeArchiveEntry();

        // Assert
        // Expected output:
        // 1. Archive magic: "!<arch>\n" (8 bytes)
        // 2. Long filename entry header for "//": (60 bytes)
        // 3. Long filename itself: (45 bytes)
        // 4. Padding for long filename: (1 byte, since 45 is odd)
        // 5. Actual entry header (name is truncated): (60 bytes)
        final int expectedSize = 8 + 60 + longName.length() + 1 + 60;
        assertEquals(expectedSize, arOut.getBytesWritten());

        String archiveContents = outputBuffer.toString(StandardCharsets.US_ASCII);
        assertTrue("Archive should start with magic header", archiveContents.startsWith("!<arch>\n"));
        assertTrue("Archive should contain the long filename entry", archiveContents.contains(longName));
    }

    @Test
    public void putArchiveEntryWithBsdLongFileModeHandlesNameWithSpaces() throws IOException {
        // Arrange
        arOut.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
        String nameWithSpace = "a name with space"; // 17 chars, but would trigger on space alone
        ArArchiveEntry entry = new ArArchiveEntry(nameWithSpace, 0);

        // Act
        arOut.putArchiveEntry(entry);
        arOut.closeArchiveEntry();

        // Assert
        String archiveContents = outputBuffer.toString(StandardCharsets.US_ASCII);
        assertTrue("BSD mode should be triggered for names with spaces", archiveContents.contains(nameWithSpace));
    }

    @Test
    public void putArchiveEntryWithTooLargeSizeThrowsException() {
        // Arrange
        // The AR header supports a size up to 9,999,999,999 (10 digits).
        ArArchiveEntry entry = new ArArchiveEntry("short-name.txt", 10_000_000_000L);

        // Act & Assert
        IOException e = assertThrows(IOException.class, () -> arOut.putArchiveEntry(entry));
        assertEquals("Size too long", e.getMessage());
    }

    @Test
    public void finishOrCloseWithUnclosedEntryThrowsException() throws IOException {
        // Arrange
        arOut.putArchiveEntry(new ArArchiveEntry("unclosed.txt", 10));

        // Act & Assert
        IOException e = assertThrows("finish() should fail", IOException.class, () -> arOut.finish());
        assertEquals("This archive contains unclosed entries.", e.getMessage());

        // Re-initialize for close() test
        setUp();
        arOut.putArchiveEntry(new ArArchiveEntry("unclosed.txt", 10));
        IOException e2 = assertThrows("close() should fail", IOException.class, () -> arOut.close());
        assertEquals("This archive contains unclosed entries.", e2.getMessage());
    }

    @Test
    public void putNextEntryWhenPreviousEntrySizeMismatchThrowsException() throws IOException {
        // Arrange
        ArArchiveEntry entry1 = new ArArchiveEntry("entry1.txt", 5); // Expects 5 bytes
        arOut.putArchiveEntry(entry1);
        arOut.write(new byte[3]); // Writes only 3 bytes

        ArArchiveEntry entry2 = new ArArchiveEntry("entry2.txt", 10);

        // Act & Assert
        // This implicitly calls closeArchiveEntry() on entry1, which detects the mismatch.
        try {
            arOut.putArchiveEntry(entry2);
            fail("Expected an IOException due to entry size mismatch.");
        } catch (IOException e) {
            assertEquals("Length does not match entry (5 != 3", e.getMessage());
        }
    }

    @Test
    public void closeArchiveEntryWithoutAnActiveEntryThrowsException() throws IOException {
        // Arrange
        // No entry has been put.

        // Act & Assert
        IOException e = assertThrows(IOException.class, () -> arOut.closeArchiveEntry());
        assertEquals("No current entry to close", e.getMessage());
    }

    @Test
    public void writingToFinishedStreamThrowsException() throws IOException {
        // Arrange
        arOut.finish();

        // Act & Assert
        IOException e = assertThrows(IOException.class, () -> arOut.putArchiveEntry(new ArArchiveEntry("test.txt", 0)));
        assertEquals("Stream has already been finished.", e.getMessage());
    }

    @Test
    public void closeIsIdempotent() throws IOException {
        // Arrange
        arOut.finish();
        arOut.close();

        // Act & Assert
        // Calling close() again should not throw an exception.
        arOut.close();
    }

    @Test
    public void createArchiveEntryFromFilePopulatesMetadata() throws IOException {
        // Arrange
        File testFile = tempFolder.newFile("test.txt");
        try (OutputStream fos = new FileOutputStream(testFile)) {
            fos.write(new byte[123]);
        }
        // Set a known modification time (note: file system precision may vary)
        long lastModified = System.currentTimeMillis() - TimeUnit.DAYS.toMillis(1);
        testFile.setLastModified(lastModified);

        // Act
        ArArchiveEntry entry = arOut.createArchiveEntry(testFile, "archive-name.txt");

        // Assert
        assertEquals("archive-name.txt", entry.getName());
        assertEquals(123, entry.getSize());
        // Check modification time, allowing for file system precision loss (e.g., to the nearest second)
        assertEquals(lastModified / 1000, entry.getLastModified());
    }

    @Test
    public void createArchiveEntryFromPathPopulatesMetadata() throws IOException {
        // Arrange
        Path testPath = tempFolder.newFile("path_test.txt").toPath();
        Files.write(testPath, new byte[456]);

        // Act
        ArArchiveEntry entry = arOut.createArchiveEntry(testPath, "path-entry.txt");

        // Assert
        assertEquals("path-entry.txt", entry.getName());
        assertEquals(456, entry.getSize());
    }
}