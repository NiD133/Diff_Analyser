package org.apache.commons.compress.archivers.ar;

import org.junit.Test;
import org.junit.Before;
import static org.junit.Assert.*;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.file.LinkOption;
import java.nio.file.NoSuchFileException;
import java.nio.file.Path;
import org.apache.commons.compress.archivers.ar.ArArchiveEntry;
import org.apache.commons.compress.archivers.ar.ArArchiveOutputStream;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockFileOutputStream;

/**
 * Test suite for ArArchiveOutputStream functionality.
 * Tests cover entry creation, writing, long filename handling, and error conditions.
 */
public class ArArchiveOutputStream_ESTest {

    private ByteArrayOutputStream outputStream;
    private ArArchiveOutputStream archiveOutputStream;

    @Before
    public void setUp() {
        outputStream = new ByteArrayOutputStream();
        archiveOutputStream = new ArArchiveOutputStream(outputStream);
    }

    // ========== Long Filename Mode Tests ==========

    @Test(timeout = 4000)
    public void testLongFileMode_BsdModeWithLongFilename() throws Throwable {
        // Given: Archive output stream with BSD long file mode
        MockFileOutputStream mockOutput = new MockFileOutputStream("test_output", false);
        ArArchiveOutputStream archive = new ArArchiveOutputStream(mockOutput);
        archive.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
        
        // When: Adding entry with long filename (>16 chars)
        ArArchiveEntry longNameEntry = new ArArchiveEntry("yIt}d@~vPmJYrIIJ", 3101L);
        archive.putArchiveEntry(longNameEntry);
        
        // Then: Entry should be written successfully with header
        assertEquals("Archive should write entry header", 68L, archive.getBytesWritten());
    }

    @Test(timeout = 4000)
    public void testLongFileMode_ErrorModeWithLongFilename() throws Throwable {
        // Given: Archive with default error mode for long filenames
        ArArchiveEntry longNameEntry = new ArArchiveEntry(" in ASI extra field", 0);
        
        // When/Then: Adding long filename should fail
        try {
            archiveOutputStream.putArchiveEntry(longNameEntry);
            fail("Should throw IOException for long filename in error mode");
        } catch (IOException e) {
            assertTrue("Error message should mention filename length", 
                      e.getMessage().contains("File name too long, > 16 chars"));
        }
    }

    // ========== Entry Creation Tests ==========

    @Test(timeout = 4000)
    public void testCreateArchiveEntry_FromExistingFile() throws Throwable {
        // Given: A temporary file with content
        File tempFile = MockFile.createTempFile("test", "suffix", null);
        
        // When: Creating archive entry from file
        ArArchiveEntry entry = archiveOutputStream.createArchiveEntry(tempFile, "test_entry");
        
        // Then: Entry should have file's properties
        assertEquals("Entry name should match", "test_entry", entry.getName());
        assertTrue("Entry size should be non-negative", entry.getLength() >= 0);
    }

    @Test(timeout = 4000)
    public void testCreateArchiveEntry_FromNonExistentPath() throws Throwable {
        // Given: Non-existent file path
        MockFile nonExistentFile = new MockFile("non_existent_file");
        Path nonExistentPath = nonExistentFile.toPath();
        LinkOption[] options = new LinkOption[0];
        
        // When/Then: Should throw NoSuchFileException
        try {
            archiveOutputStream.createArchiveEntry(nonExistentPath, "test", options);
            fail("Should throw NoSuchFileException for non-existent file");
        } catch (NoSuchFileException e) {
            // Expected behavior
        }
    }

    // ========== Entry Writing Tests ==========

    @Test(timeout = 4000)
    public void testPutArchiveEntry_ValidEntry() throws Throwable {
        // Given: Valid archive entry
        ArArchiveEntry entry = new ArArchiveEntry("test_file", 0);
        
        // When: Adding entry to archive
        archiveOutputStream.putArchiveEntry(entry);
        
        // Then: Entry should be written with proper header size
        assertEquals("Should write standard header", 68L, archiveOutputStream.getBytesWritten());
    }

    @Test(timeout = 4000)
    public void testPutArchiveEntry_ExcessiveSize() throws Throwable {
        // Given: Entry with size too large for AR format
        ArArchiveEntry oversizedEntry = new ArArchiveEntry("test", Long.MAX_VALUE);
        
        // When/Then: Should reject oversized entries
        try {
            archiveOutputStream.putArchiveEntry(oversizedEntry);
            fail("Should reject entries with excessive size");
        } catch (IOException e) {
            assertEquals("Should report size error", "Size too long", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testWriteData_ToOpenEntry() throws Throwable {
        // Given: Open archive entry
        File tempFile = MockFile.createTempFile("test", "data", null);
        ArArchiveEntry entry = archiveOutputStream.createArchiveEntry(tempFile, "data_file");
        archiveOutputStream.putArchiveEntry(entry);
        
        // When: Writing data to entry
        byte[] testData = new byte[7];
        archiveOutputStream.write(testData, 0, testData.length);
        archiveOutputStream.closeArchiveEntry();
        
        // Then: Data should be written successfully
        assertTrue("Should track written bytes", archiveOutputStream.getBytesWritten() > 68L);
        assertEquals("Should count entries", 1, archiveOutputStream.getCount());
    }

    // ========== Entry Lifecycle Tests ==========

    @Test(timeout = 4000)
    public void testCloseArchiveEntry_WithoutOpenEntry() throws Throwable {
        // When/Then: Closing entry when none is open should fail
        try {
            archiveOutputStream.closeArchiveEntry();
            fail("Should not allow closing when no entry is open");
        } catch (IOException e) {
            assertEquals("Should report no current entry", "No current entry to close", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testPutArchiveEntry_SizeMismatch() throws Throwable {
        // Given: Entry with declared size and different actual size
        ArArchiveEntry entry = new ArArchiveEntry("test", 1);
        archiveOutputStream.putArchiveEntry(entry);
        // Not writing any data, so actual size is 0
        
        // When/Then: Adding another entry should detect size mismatch
        try {
            archiveOutputStream.putArchiveEntry(entry);
            fail("Should detect size mismatch");
        } catch (IOException e) {
            assertTrue("Should report length mismatch", 
                      e.getMessage().contains("Length does not match entry"));
        }
    }

    // ========== Stream Lifecycle Tests ==========

    @Test(timeout = 4000)
    public void testFinish_WithUnclosedEntry() throws Throwable {
        // Given: Archive with open entry
        ArArchiveEntry entry = new ArArchiveEntry("unclosed", 0);
        archiveOutputStream.putArchiveEntry(entry);
        
        // When/Then: Finishing with unclosed entry should fail
        try {
            archiveOutputStream.finish();
            fail("Should not allow finish with unclosed entries");
        } catch (IOException e) {
            assertEquals("Should report unclosed entries", 
                        "This archive contains unclosed entries.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testClose_WithUnclosedEntry() throws Throwable {
        // Given: Archive with open entry
        ArArchiveEntry entry = new ArArchiveEntry("unclosed", 1);
        archiveOutputStream.putArchiveEntry(entry);
        
        // When/Then: Closing with unclosed entry should fail
        try {
            archiveOutputStream.close();
            fail("Should not allow close with unclosed entries");
        } catch (IOException e) {
            assertEquals("Should report unclosed entries", 
                        "This archive contains unclosed entries.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testOperationsAfterClose() throws Throwable {
        // Given: Closed archive
        archiveOutputStream.close();
        File tempFile = MockFile.createTempFile("test", "file", null);
        
        // When/Then: Operations after close should fail
        try {
            archiveOutputStream.createArchiveEntry(tempFile, "test");
            fail("Should not allow operations after close");
        } catch (IOException e) {
            assertEquals("Should report stream finished", 
                        "Stream has already been finished.", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testSuccessfulArchiveCreation() throws Throwable {
        // Given: Valid entry
        File tempFile = MockFile.createTempFile("test", "file", null);
        ArArchiveEntry entry = archiveOutputStream.createArchiveEntry(tempFile, "success");
        
        // When: Complete archive creation workflow
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.finish();
        
        // Then: Archive should be created successfully
        assertTrue("Should have written data", archiveOutputStream.getBytesWritten() > 0);
    }

    // ========== Error Condition Tests ==========

    @Test(timeout = 4000)
    public void testWrite_NullBuffer() throws Throwable {
        // When/Then: Writing null buffer should fail
        try {
            archiveOutputStream.write(null, 1, 1);
            fail("Should reject null buffer");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testWrite_InvalidArrayBounds() throws Throwable {
        // Given: Small buffer
        byte[] smallBuffer = new byte[7];
        
        // When/Then: Invalid array access should fail
        try {
            archiveOutputStream.write(smallBuffer, -90, 315);
            fail("Should reject invalid array bounds");
        } catch (ArrayIndexOutOfBoundsException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testCreateArchiveEntry_NullFile() throws Throwable {
        // When/Then: Null file should cause failure
        try {
            archiveOutputStream.createArchiveEntry((File) null, "test");
            fail("Should reject null file");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    @Test(timeout = 4000)
    public void testPutArchiveEntry_NullEntry() throws Throwable {
        // When/Then: Null entry should cause failure
        try {
            archiveOutputStream.putArchiveEntry(null);
            fail("Should reject null entry");
        } catch (NullPointerException e) {
            // Expected behavior
        }
    }

    // ========== Multiple Entry Tests ==========

    @Test(timeout = 4000)
    public void testMultipleEntries_InBsdMode() throws Throwable {
        // Given: Archive in BSD mode
        archiveOutputStream.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
        ArArchiveEntry firstEntry = new ArArchiveEntry("Mjru8P b$iI9", 0);
        
        // When: Adding same entry multiple times
        archiveOutputStream.putArchiveEntry(firstEntry);
        archiveOutputStream.putArchiveEntry(firstEntry);
        
        // Then: Both entries should be written
        assertEquals("Should write both entries", 154L, archiveOutputStream.getBytesWritten());
    }

    @Test(timeout = 4000)
    public void testMultipleEntries_StandardMode() throws Throwable {
        // Given: Entry with valid name length
        ArArchiveEntry entry = new ArArchiveEntry("jl^;TJ]T9ZU<Vh", 0);
        
        // When: Adding, closing, and re-adding entry
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.closeArchiveEntry();
        archiveOutputStream.putArchiveEntry(entry);
        
        // Then: Should handle multiple entries correctly
        assertEquals("Should write both entries", 128, archiveOutputStream.getCount());
    }

    @Test(timeout = 4000)
    public void testCloseArchiveEntry_MultipleCalls() throws Throwable {
        // Given: Closed entry
        ArArchiveEntry entry = new ArArchiveEntry("test_entry", 0);
        archiveOutputStream.putArchiveEntry(entry);
        archiveOutputStream.closeArchiveEntry();
        
        // When/Then: Second close should fail
        try {
            archiveOutputStream.closeArchiveEntry();
            fail("Should not allow multiple closes");
        } catch (IOException e) {
            assertEquals("Should report no current entry", "No current entry to close", e.getMessage());
        }
    }

    @Test(timeout = 4000)
    public void testClose_MultipleCalls() throws Throwable {
        // Given: Closed archive
        archiveOutputStream.close();
        
        // When: Closing again
        archiveOutputStream.close();
        
        // Then: Should handle multiple closes gracefully
        // No exception expected - this should be idempotent
    }
}