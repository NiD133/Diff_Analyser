package org.apache.commons.io;

import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.nio.file.NoSuchFileException;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import java.nio.charset.StandardCharsets; //Explicitly import charset

/**
 * Test case focusing on the behavior of FileUtils.lineIterator when a file is missing.
 * This test ensures that the method throws the correct exception when attempting to read a non-existent file.
 */
public class MissingFileLineIteratorTest {

    @TempDir
    public File temporaryFolder; // JUnit's way to provide temporary folders

    private static final String UTF_8 = StandardCharsets.UTF_8.name(); // Define UTF-8 encoding as a constant

    @Test
    public void testLineIteratorThrowsExceptionForMissingFile() {
        // 1. Arrange:  Create a File object representing a file that doesn't exist.
        File missingFile = new File(temporaryFolder, "nonexistent_file.txt");

        // 2. Act & Assert:  Call FileUtils.lineIterator with the missing file and UTF-8 encoding.
        //    Verify that it throws a NoSuchFileException.
        assertThrows(NoSuchFileException.class, () -> {
            FileUtils.lineIterator(missingFile, UTF_8);
        }, "FileUtils.lineIterator should throw NoSuchFileException when the file does not exist.");
    }
}