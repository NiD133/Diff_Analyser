package org.apache.commons.compress.archivers.ar;

import org.apache.commons.compress.AbstractTest;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

/**
 * Tests for {@link ArArchiveOutputStream} focusing on long file name handling.
 */
public class ArArchiveOutputStreamTestTest2 extends AbstractTest {

    /**
     * Verifies that the BSD dialect correctly handles file names longer than the standard 16-character limit.
     */
    @Test
    void shouldCreateArchiveWithLongFileNameWhenBsdDialectIsUsed() throws Exception {
        // Arrange
        // The standard AR format has a 16-character limit for file names.
        // This name is intentionally longer to test the BSD extension.
        final String longFileName = "this_is_a_long_file_name.txt"; // 30 characters
        final String fileContent = "Hello, world!\n";
        final byte[] contentBytes = fileContent.getBytes(StandardCharsets.UTF_8);

        final File archiveFile = createTempFile();

        // Act
        try (ArArchiveOutputStream outputStream = new ArArchiveOutputStream(Files.newOutputStream(archiveFile.toPath()))) {
            // Configure the stream to use the BSD dialect for long file name support.
            outputStream.setLongFileMode(ArArchiveOutputStream.LONGFILE_BSD);

            final ArArchiveEntry entry = new ArArchiveEntry(longFileName, contentBytes.length);
            outputStream.putArchiveEntry(entry);
            outputStream.write(contentBytes);
            outputStream.closeArchiveEntry();
        }

        // Assert
        // The checkArchiveContent helper from AbstractTest verifies the archive's contents.
        final List<String> expectedEntryNames = List.of(longFileName);
        checkArchiveContent(archiveFile, expectedEntryNames);
    }
}