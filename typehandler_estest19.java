package org.apache.commons.cli;

import org.junit.Test;

/**
 * Tests for the {@link TypeHandler} class, focusing on file handling.
 */
public class TypeHandlerTest {

    /**
     * Verifies that calling openFile with a path to a non-existent file
     * correctly throws a ParseException.
     */
    @Test(expected = ParseException.class)
    public void openFile_shouldThrowParseException_whenFileDoesNotExist() throws ParseException {
        // Arrange: Define a path that is highly unlikely to exist.
        final String nonExistentFilePath = "path/to/a/non_existent_file.txt";

        // Act: Attempt to open the non-existent file.
        // The @Test(expected) annotation will assert that a ParseException is thrown.
        TypeHandler.openFile(nonExistentFilePath);
    }
}