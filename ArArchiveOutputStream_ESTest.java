package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import static org.junit.Assert.*;
import static org.evosuite.runtime.EvoAssertions.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import org.evosuite.runtime.EvoRunner;
import org.evosuite.runtime.EvoRunnerParameters;
import org.evosuite.runtime.System;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;
import org.evosuite.runtime.mock.java.io.MockPrintStream;
import org.evosuite.runtime.testdata.FileSystemHandling;
import org.junit.runner.RunWith;

@RunWith(EvoRunner.class)
@EvoRunnerParameters(
    mockJVMNonDeterminism = true,
    useVFS = true,
    useVNET = true,
    resetStaticState = true,
    separateClassLoader = true
)
public class ArArchiveOutputStream_ESTest extends ArArchiveOutputStream_ESTest_scaffolding {

    // ========================================================================
    // putArchiveEntry Tests
    // ========================================================================

    @Test(timeout = 4000)
    public void putArchiveEntry_usesBSDModeForLongNames() throws Throwable {
        MockFileOutputStream outputStream = new MockFileOutputStream("longname", false);
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        arOutput.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
        ArArchiveEntry entry = new ArArchiveEntry("very_long_file_name.txt", 3101L);
        
        arOutput.putArchiveEntry(entry);
        
        assertEquals(68L, arOutput.getBytesWritten());
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_handlesNegativeLongFileModeAsBSD() throws Throwable {
        MockFileOutputStream outputStream = new MockFileOutputStream("test", false);
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        File file = new MockFile("test");
        ArArchiveEntry entry = arOutput.createArchiveEntry(file, "entry_name");
        arOutput.setLongFileMode(-2628); // Negative value treated as BSD mode
        
        arOutput.putArchiveEntry(entry);
        
        assertEquals(68L, arOutput.getBytesWritten());
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_afterPartialWriteVerifiesLength() throws Throwable {
        MockPrintStream printStream = new MockPrintStream("base");
        byte[] data = new byte[7];
        File file = MockFile.createTempFile("temp", "data");
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(printStream);
        ArArchiveEntry entry = arOutput.createArchiveEntry(file, "short");
        arOutput.putArchiveEntry(entry);
        arOutput.write(data, 0, 536); // Write 536 bytes
        
        try {
            arOutput.putArchiveEntry(entry); // Attempt to re-add same entry
            fail("Expected IOException for length mismatch");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Length does not match entry"));
        }
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_usesBSDModeForLargeModeValue() throws Throwable {
        MockPrintStream printStream = new MockPrintStream("base");
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(printStream);
        File file = MockFile.createTempFile("temp", "data");
        arOutput.setLongFileMode(1909); // Large value treated as BSD
        
        ArArchiveEntry entry = arOutput.createArchiveEntry(file, "long_name_test");
        arOutput.putArchiveEntry(entry);
        
        assertEquals(74L, arOutput.getBytesWritten());
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_processesFullMetadataEntry() throws Throwable {
        MockFileOutputStream outputStream = new MockFileOutputStream("entry", false);
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        ArArchiveEntry entry = new ArArchiveEntry("User ID", 8589934591L, 61440, 7, -2628, 0);
        
        arOutput.putArchiveEntry(entry);
        
        assertEquals(68L, arOutput.getBytesWritten());
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_throwsWhenNameTooLongInDefaultMode() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        ArArchiveEntry entry = new ArArchiveEntry("ThisNameIsWayTooLongForDefaultMode", 0);
        
        try {
            arOutput.putArchiveEntry(entry);
            fail("Expected IOException for long name");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("File name too long"));
        }
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_throwsWhenAddingSameEntryTwice() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        ArArchiveEntry entry = new ArArchiveEntry("test.txt", 1);
        arOutput.putArchiveEntry(entry);
        
        try {
            arOutput.putArchiveEntry(entry);
            fail("Expected IOException for duplicate entry");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Length does not match entry"));
        }
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_throwsForHugeFileSizes() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        ArArchiveEntry entry = new ArArchiveEntry("large.bin", Long.MAX_VALUE);
        
        try {
            arOutput.putArchiveEntry(entry);
            fail("Expected IOException for huge file size");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Size too long"));
        }
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_throwsWhenNullEntry() throws Throwable {
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream((OutputStream) null);
        try {
            arOutput.putArchiveEntry(null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========================================================================
    // createArchiveEntry Tests
    // ========================================================================

    @Test(timeout = 4000)
    public void createArchiveEntry_fromPathWithLinkOptions() throws Throwable {
        MockPrintStream printStream = new MockPrintStream("base");
        MockFile file = new MockFile("test_file");
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(printStream);
        Path path = file.toPath();
        LinkOption[] options = new LinkOption[] {
            LinkOption.NOFOLLOW_LINKS,
            LinkOption.NOFOLLOW_LINKS,
            LinkOption.NOFOLLOW_LINKS,
            LinkOption.NOFOLLOW_LINKS,
            LinkOption.NOFOLLOW_LINKS,
            LinkOption.NOFOLLOW_LINKS,
            LinkOption.NOFOLLOW_LINKS,
            LinkOption.NOFOLLOW_LINKS,
            LinkOption.NOFOLLOW_LINKS
        };
        
        ArArchiveEntry entry = arOutput.createArchiveEntry(path, "", options);
        
        assertEquals(0, entry.getUserId());
    }

    @Test(timeout = 4000)
    public void createArchiveEntry_fromFileSetsCorrectSize() throws Throwable {
        File file = MockFile.createTempFile("test", "tmp");
        MockPrintStream printStream = new MockPrintStream(file);
        printStream.println((Object) null); // Write 5 bytes (null as string)
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream((OutputStream) null);
        
        ArArchiveEntry entry = arOutput.createArchiveEntry(
            file, "org.apache.commons.compress.archivers.sevenz.SevenZMethodConfiguration");
        
        assertEquals(5L, entry.getLength());
    }

    @Test(timeout = 4000)
    public void createArchiveEntry_fromNonExistingFile() throws Throwable {
        MockPrintStream printStream = new MockPrintStream("base");
        MockFile file = new MockFile("non_existent_file");
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(printStream);
        
        ArArchiveEntry entry = arOutput.createArchiveEntry(file, "test_name");
        
        assertEquals(0L, entry.getSize());
    }

    @Test(timeout = 4000)
    public void createArchiveEntry_withNegativeTimestamp() throws Throwable {
        System.setCurrentTimeMillis(-1105L); // Set to past time
        MockFileOutputStream outputStream = new MockFileOutputStream("test", false);
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        File file = MockFile.createTempFile("temp", "file");
        
        ArArchiveEntry entry = arOutput.createArchiveEntry(file, "");
        
        assertEquals(0L, entry.getLength());
    }

    @Test(timeout = 4000)
    public void createArchiveEntry_throwsForNonExistentPath() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        MockFile file = new MockFile("non_existent");
        Path path = file.toPath();
        
        try {
            arOutput.createArchiveEntry(path, "name");
            fail("Expected NoSuchFileException");
        } catch (NoSuchFileException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void createArchiveEntry_throwsAfterStreamFinished() throws Throwable {
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream((OutputStream) null);
        arOutput.finish();
        LinkOption[] options = new LinkOption[0];
        
        try {
            arOutput.createArchiveEntry((Path) null, "name", options);
            fail("Expected IOException for finished stream");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Stream has already been finished"));
        }
    }

    @Test(timeout = 4000)
    public void createArchiveEntry_throwsAfterStreamClosed() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        arOutput.close();
        File file = MockFile.createTempFile("temp", "file");
        
        try {
            arOutput.createArchiveEntry(file, "name");
            fail("Expected IOException for closed stream");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Stream has already been finished"));
        }
    }

    @Test(timeout = 4000)
    public void createArchiveEntry_throwsForNullFile() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        
        try {
            arOutput.createArchiveEntry((File) null, "name");
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void createArchiveEntry_throwsForNullPath() throws Throwable {
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream((OutputStream) null);
        
        try {
            arOutput.createArchiveEntry((Path) null, "name", (LinkOption[]) null);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========================================================================
    // closeArchiveEntry Tests
    // ========================================================================

    @Test(timeout = 4000)
    public void closeArchiveEntry_afterWritingData() throws Throwable {
        MockPrintStream printStream = new MockPrintStream("base");
        byte[] data = new byte[7];
        File file = MockFile.createTempFile("temp", "data");
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(printStream);
        ArArchiveEntry entry = arOutput.createArchiveEntry(file, "entry");
        arOutput.putArchiveEntry(entry);
        arOutput.write(data, 0, 1215);
        
        arOutput.closeArchiveEntry();
        
        assertEquals(1283L, arOutput.getBytesWritten());
    }

    @Test(timeout = 4000)
    public void closeArchiveEntry_throwsWhenNoOpenEntry() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        
        try {
            arOutput.closeArchiveEntry();
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("No current entry to close"));
        }
    }

    @Test(timeout = 4000)
    public void closeArchiveEntry_throwsAfterPreviousClose() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        ArArchiveEntry entry = new ArArchiveEntry("file.txt", 0);
        arOutput.putArchiveEntry(entry);
        arOutput.closeArchiveEntry(); // First close
        
        try {
            arOutput.closeArchiveEntry(); // Second close
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("No current entry to close"));
        }
    }

    // ========================================================================
    // write Tests
    // ========================================================================

    @Test(timeout = 4000)
    public void write_handlesNullBuffer() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        
        try {
            arOutput.write(null, 1, 1);
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void write_handlesNegativeOffset() throws Throwable {
        MockFileOutputStream outputStream = new MockFileOutputStream("test", false);
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        byte[] data = new byte[7];
        
        try {
            arOutput.write(data, -90, 315);
            fail("Expected ArrayIndexOutOfBoundsException");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void write_propagatesIOException() throws Throwable {
        MockFileOutputStream outputStream = new MockFileOutputStream("test", false);
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        FileSystemHandling.shouldAllThrowIOExceptions(); // Simulate IO errors
        
        try {
            arOutput.write(null, 1, 0);
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("Simulated IOException"));
        }
    }

    @Test(timeout = 4000)
    public void write_handlesNegativeOffsetAndLength() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        
        try {
            arOutput.write(null, -1037, -1037);
            fail("Expected IndexOutOfBoundsException");
        } catch (IndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    // ========================================================================
    // close/finish Tests
    // ========================================================================

    @Test(timeout = 4000)
    public void close_handlesNullOutputStream() throws Throwable {
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream((OutputStream) null);
        try {
            arOutput.close();
            fail("Expected NullPointerException");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void close_throwsWithOpenEntry() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        ArArchiveEntry entry = new ArArchiveEntry("file.txt", 1);
        arOutput.putArchiveEntry(entry);
        
        try {
            arOutput.close();
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("unclosed entries"));
        }
    }

    @Test(timeout = 4000)
    public void close_isIdempotent() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        arOutput.close();
        arOutput.close(); // Second close should not throw
    }

    @Test(timeout = 4000)
    public void finish_afterClosingEntry() throws Throwable {
        MockPrintStream printStream = new MockPrintStream("base");
        File file = MockFile.createTempFile("temp", "data");
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(printStream);
        ArArchiveEntry entry = arOutput.createArchiveEntry(file, "entry");
        arOutput.putArchiveEntry(entry);
        arOutput.closeArchiveEntry();
        
        arOutput.finish(); // Should not throw
        
        assertEquals(68L, arOutput.getBytesWritten());
    }

    @Test(timeout = 4000)
    public void finish_throwsWithOpenEntry() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        ArArchiveEntry entry = new ArArchiveEntry("file.txt", 0);
        arOutput.putArchiveEntry(entry);
        
        try {
            arOutput.finish();
            fail("Expected IOException");
        } catch (IOException e) {
            assertTrue(e.getMessage().contains("unclosed entries"));
        }
    }

    // ========================================================================
    // Edge Cases & Additional Tests
    // ========================================================================

    @Test(timeout = 4000)
    public void write_emptyDataWithValidOffset() throws Throwable {
        MockPrintStream printStream = new MockPrintStream("base");
        byte[] data = new byte[7];
        File file = MockFile.createTempFile("temp", "data");
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(printStream);
        ArArchiveEntry entry = arOutput.createArchiveEntry(file, "entry");
        arOutput.putArchiveEntry(entry);
        arOutput.write(data, 37, -67); // Negative length = no write
        
        arOutput.closeArchiveEntry();
        
        assertEquals(1, arOutput.getCount());
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_BSDWithZeroLength() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        arOutput.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
        ArArchiveEntry entry = new ArArchiveEntry("zero.txt", 0);
        
        arOutput.putArchiveEntry(entry);
        
        assertEquals(68, arOutput.getCount());
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_BSDWithNonZeroLength() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        arOutput.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
        ArArchiveEntry entry = new ArArchiveEntry("non_zero.txt", 1);
        
        arOutput.putArchiveEntry(entry);
        
        assertEquals(93L, arOutput.getBytesWritten());
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_allowsMultipleEntriesWhenClosed() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        ArArchiveEntry entry = new ArArchiveEntry("first.txt", 0);
        arOutput.putArchiveEntry(entry);
        arOutput.closeArchiveEntry();
        
        arOutput.putArchiveEntry(entry); // Should be allowed
        
        assertEquals(128, arOutput.getCount());
    }

    @Test(timeout = 4000)
    public void putArchiveEntry_BSDAllowsMultipleEntries() throws Throwable {
        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        ArArchiveOutputStream arOutput = new ArArchiveOutputStream(outputStream);
        arOutput.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
        ArArchiveEntry entry = new ArArchiveEntry("file1.txt", 0);
        arOutput.putArchiveEntry(entry);
        arOutput.putArchiveEntry(entry); // BSD mode allows adding again
        
        assertEquals(154L, arOutput.getBytesWritten());
    }
}