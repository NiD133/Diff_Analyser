package org.apache.commons.cli;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;

import java.io.File;
import org.junit.Test;

/**
 * Tests for {@link TypeHandler}.
 */
public class TypeHandlerTest {

    /**
     * Tests that createFile returns a File object representing the given path,
     * without creating an actual file on the filesystem.
     */
    @Test
    public void createFileShouldReturnFileObjectForGivenPath() {
        // Arrange
        final String nonExistentFilePath = "path/to/a/non_existent_file.txt";

        // Act
        final File resultFile = TypeHandler.createFile(nonExistentFilePath);

        // Assert
        // The method should return a non-null File object
        assertNotNull("The created file object should not be null.", resultFile);

        // The File object's path should match the input string
        assertEquals("The file path should match the input string.", nonExistentFilePath, resultFile.getPath());

        // The File object represents a path that does not exist on the filesystem
        assertFalse("The file should not exist on the filesystem.", resultFile.exists());

        // A non-existent path is, by definition, not a directory
        assertFalse("A non-existent file should not be a directory.", resultFile.isDirectory());
    }
}