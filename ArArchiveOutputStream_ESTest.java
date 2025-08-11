package org.apache.commons.compress.archivers.ar;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;

import static org.junit.Assert.*;

public class ArArchiveOutputStreamTest {

    private ByteArrayOutputStream out;
    private ArArchiveOutputStream arOut;

    @Before
    public void setUp() {
        out = new ByteArrayOutputStream();
        arOut = new ArArchiveOutputStream(out);
    }

    @After
    public void tearDown() throws IOException {
        // Close quietly if still open; some tests already close or finish.
        try {
            arOut.close();
        } catch (IOException ignored) {
            // Some tests intentionally provoke IOExceptions on close (e.g., unclosed entries).
        }
    }

    // Helper: create a temp file with given bytes and return it
    private static File newTempFileWithContent(byte[] content) throws IOException {
        Path p = Files.createTempFile("ar-test-", ".bin");
        Files.write(p, content);
        p.toFile().deleteOnExit();
        return p.toFile();
    }

    @Test
    public void putEntry_withShortName_writesHeader() throws Exception {
        // Given a short file name and zero size
        ArArchiveEntry e = new ArArchiveEntry("hello.txt", 0);

        // When
        long before = arOut.getBytesWritten();
        arOut.putArchiveEntry(e);

        // Then
        assertTrue("bytesWritten should increase after writing entry header",
                arOut.getBytesWritten() > before);

        // Cleanup
        arOut.closeArchiveEntry();
        arOut.finish();
    }

    @Test
    public void putEntry_withLongName_failsByDefault() throws Exception {
        // Given a name longer than the 16-char ar limit
        ArArchiveEntry e = new ArArchiveEntry("this-file-name-is-way-too-long", 0);

        // When / Then
        try {
            arOut.putArchiveEntry(e);
            fail("Expected IOException for long file name in default mode");
        } catch (IOException ex) {
            assertTrue(ex.getMessage().contains("File name too long"));
        }
    }

    @Test
    public void putEntry_withLongName_succeedsInBsdMode() throws Exception {
        // Given BSD long-file mode
        arOut.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
        ArArchiveEntry e = new ArArchiveEntry("this-file-name-is-way-too-long", 0);

        // When / Then
        arOut.putArchiveEntry(e);
        arOut.closeArchiveEntry();
        arOut.finish(); // should not throw
    }

    @Test
    public void putEntry_withTooLargeSize_fails() throws Exception {
        // Given an entry with an extremely large size
        ArArchiveEntry e = new ArArchiveEntry("huge.bin", Long.MAX_VALUE);

        // When / Then
        try {
            arOut.putArchiveEntry(e);
            fail("Expected IOException for excessive size");
        } catch (IOException ex) {
            assertTrue(ex.getMessage().toLowerCase().contains("size"));
        }
    }

    @Test
    public void startingNextEntryWithoutWritingExpectedBytes_failsWithLengthMismatch() throws Exception {
        // Given an entry that claims to be 1 byte
        ArArchiveEntry e = new ArArchiveEntry("onebyte", 1);
        arOut.putArchiveEntry(e);
        // Don't write any data

        // When starting a new entry, length mismatch should be detected
        try {
            arOut.putArchiveEntry(new ArArchiveEntry("next", 0));
            fail("Expected IOException due to length mismatch on previous entry");
        } catch (IOException ex) {
            assertTrue(ex.getMessage().contains("Length does not match entry"));
        }
    }

    @Test
    public void closeArchiveEntry_withoutOpenEntry_fails() throws Exception {
        // Given no entry is open

        // When / Then
        try {
            arOut.closeArchiveEntry();
            fail("Expected IOException when no entry is open");
        } catch (IOException ex) {
            assertTrue(ex.getMessage().contains("No current entry to close"));
        }
    }

    @Test
    public void finish_withUnclosedEntry_fails() throws Exception {
        // Given an open entry that hasn't been closed
        ArArchiveEntry e = new ArArchiveEntry("unfinished", 0);
        arOut.putArchiveEntry(e);

        // When / Then
        try {
            arOut.finish();
            fail("Expected IOException because there is an unclosed entry");
        } catch (IOException ex) {
            assertTrue(ex.getMessage().contains("unclosed entries"));
        }
    }

    @Test
    public void createArchiveEntry_fromFile_usesFileLength() throws Exception {
        // Given a real file with known length
        byte[] data = {1, 2, 3, 4, 5};
        File f = newTempFileWithContent(data);

        // When
        ArArchiveEntry e = arOut.createArchiveEntry(f, "file.bin");

        // Then
        assertEquals("Entry length should match file size", data.length, e.getLength());
    }

    @Test
    public void createArchiveEntry_fromNonexistentPath_fails() throws Exception {
        // Given a path that does not exist
        Path missing = new File("definitely-does-not-exist-" + System.nanoTime()).toPath();

        // When / Then
        try {
            arOut.createArchiveEntry(missing, "missing");
            fail("Expected NoSuchFileException for non-existing path");
        } catch (NoSuchFileException expected) {
            // ok
        }
    }

    @Test
    public void write_nullArray_throwsNpe() throws Exception {
        // Given
        byte[] bytes = null;

        // When / Then
        try {
            arOut.write(bytes, 0, 0);
            fail("Expected NullPointerException when writing null array");
        } catch (NullPointerException expected) {
            // ok
        }
    }

    @Test
    public void close_canBeCalledTwice_safely() throws Exception {
        // Given a closed stream
        arOut.close();

        // When / Then: second close should not throw
        arOut.close();
    }

    @Test
    public void createArchiveEntry_afterFinish_fails() throws Exception {
        // Given a finished stream
        arOut.finish();

        // When / Then
        try {
            arOut.createArchiveEntry(newTempFileWithContent(new byte[0]), "x");
            fail("Expected IOException: stream has already been finished");
        } catch (IOException ex) {
            assertTrue(ex.getMessage().contains("already been finished"));
        }
    }

    @Test
    public void close_withUnclosedEntry_fails() throws Exception {
        // Given
        ArArchiveEntry e = new ArArchiveEntry("still-open", 0);
        arOut.putArchiveEntry(e);

        // When / Then
        try {
            arOut.close();
            fail("Expected IOException because an entry is still open");
        } catch (IOException ex) {
            assertTrue(ex.getMessage().contains("unclosed entries"));
        }
    }

    @Test
    public void finish_afterClosingEntry_succeeds_andWritesData() throws Exception {
        // Given a closed entry
        ArArchiveEntry e = new ArArchiveEntry("done", 3);
        arOut.putArchiveEntry(e);
        arOut.write(new byte[]{1, 2, 3}, 0, 3);
        arOut.closeArchiveEntry();

        // When
        long beforeFinish = arOut.getBytesWritten();
        arOut.finish();

        // Then: finish should not throw and should write something (e.g., padding)
        assertTrue("bytesWritten should be >= header+content size", arOut.getBytesWritten() >= beforeFinish);
        assertTrue("resulting archive should not be empty", out.size() > 0);
    }
}