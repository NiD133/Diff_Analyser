package org.apache.commons.compress.archivers.ar;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.util.ArrayList;
import java.util.List;

import org.apache.commons.compress.AbstractTest;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for ArArchiveOutputStream.
 */
class ArArchiveOutputStreamTest extends AbstractTest {

    /**
     * Verifies that an IOException is thrown when attempting to add an entry with a long file name
     * by default (LONGFILE_ERROR mode).
     * 
     * @throws IOException if an I/O error occurs
     */
    @Test
    void testLongFileNamesCauseExceptionByDefault() throws IOException {
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(new ByteArrayOutputStream())) {
            ArArchiveEntry entry = new ArArchiveEntry("this_is_a_long_name.txt", 0);
            assertLongFileNameExceptionIsThrown(outputStream, entry);
        }
    }

    /**
     * Verifies that an entry with a long file name can be added successfully when using
     * BSD dialect (LONGFILE_BSD mode).
     * 
     * @throws Exception if an error occurs
     */
    @Test
    void testLongFileNamesWorkUsingBSDDialect() throws Exception {
        File tempFile = createTempFile();
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(Files.newOutputStream(tempFile.toPath()))) {
            outputStream.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);
            ArArchiveEntry entry = new ArArchiveEntry("this_is_a_long_name.txt", 14);
            addEntryAndVerifyContents(outputStream, entry, tempFile);
        }
    }

    /**
     * Adds an archive entry and verifies that the expected contents are written to the output stream.
     * 
     * @param outputStream the output stream to write to
     * @param entry        the archive entry to add
     * @param file         the temporary file to write to
     * @throws Exception if an error occurs
     */
    private void addEntryAndVerifyContents(ArArchiveOutputStream outputStream, ArArchiveEntry entry, File file) throws Exception {
        outputStream.putArchiveEntry(entry);
        outputStream.write(new byte[] { 'H', 'e', 'l', 'l', 'o', ',', ' ', 'w', 'o', 'r', 'l', 'd', '!', '\n' });
        outputStream.closeArchiveEntry();
        List<String> expectedContents = new ArrayList<>();
        expectedContents.add("this_is_a_long_name.txt");
        checkArchiveContent(file, expectedContents);
    }

    /**
     * Verifies that an IOException is thrown when attempting to add an entry with a long file name.
     * 
     * @param outputStream the output stream to write to
     * @param entry        the archive entry to add
     * @throws IOException if an I/O error occurs
     */
    private void assertLongFileNameExceptionIsThrown(ArArchiveOutputStream outputStream, ArArchiveEntry entry) throws IOException {
        IOException exception = assertThrows(IOException.class, () -> outputStream.putArchiveEntry(entry));
        assertTrue(exception.getMessage().startsWith("File name too long"));
    }
}