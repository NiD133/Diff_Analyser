package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.io.File;
import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.TemporaryFolder;

/**
 * Tests for the {@link Lister} command-line application.
 *
 * This test suite focuses on verifying the input validation and error handling
 * of the Lister class, particularly for its constructor and the main execution method.
 */
public class ListerTest {

    @Rule
    public TemporaryFolder tempFolder = new TemporaryFolder();

    // --- Constructor Tests ---

    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionIfFirstArgIsNull() {
        // Arrange: Command-line arguments where the first element (the file path) is null.
        String[] argsWithNullFirstElement = {null, "zip"};

        // Act: Instantiating Lister with these arguments should fail fast.
        new Lister(false, argsWithNullFirstElement);

        // Assert: A NullPointerException is expected, handled by the @Test annotation.
    }

    @Test(expected = ArrayIndexOutOfBoundsException.class)
    public void constructorShouldThrowArrayIndexOutOfBoundsExceptionForEmptyArgs() {
        // Arrange: An empty array of command-line arguments.
        String[] emptyArgs = new String[0];

        // Act: The constructor expects at least one argument for the file path.
        new Lister(true, emptyArgs);

        // Assert: An ArrayIndexOutOfBoundsException is expected.
    }

    // --- go() Method Tests ---

    @Test(expected = NoSuchFileException.class)
    public void goShouldThrowNoSuchFileExceptionForNonExistentFile() throws Exception {
        // Arrange
        // Use a file path that is highly unlikely to exist.
        String nonExistentFilePath = "this/path/is/guaranteed/not/to/exist/archive.zip";
        String[] args = {nonExistentFilePath};
        Lister lister = new Lister(true, args);

        // Act
        lister.go();

        // Assert: A NoSuchFileException is expected.
    }

    @Test(expected = InvalidPathException.class)
    public void goShouldThrowInvalidPathExceptionForPathWithNullCharacter() throws Exception {
        // Arrange: A file path containing a null character, which is invalid on most filesystems.
        String[] args = {"invalid\u0000path.tar"};
        Lister lister = new Lister(true, args);

        // Act
        lister.go();

        // Assert: An InvalidPathException is expected.
    }

    @Test
    public void goShouldThrowArchiveExceptionForUnknownExplicitFormat() throws Exception {
        // Arrange: Create a real, empty file to ensure the failure is due to the
        // specified format being unknown, not a missing file.
        File tempFile = tempFolder.newFile("test.tmp");
        String[] args = {tempFile.getAbsolutePath(), "unsupported-archive-format"};
        Lister lister = new Lister(false, args);

        try {
            // Act
            lister.go();
            fail("Expected an ArchiveException to be thrown for an unknown format.");
        } catch (ArchiveException e) {
            // Assert
            String expectedMessage = "Archiver: unsupported-archive-format not found.";
            assertTrue("Exception message should indicate the format was not found.",
                    e.getMessage().contains(expectedMessage));
        }
    }
    
    /**
     * Tests that providing a null value in the argument list for subsequent files
     * results in a NullPointerException. This assumes Lister is capable of processing
     * multiple file arguments in a loop.
     */
    @Test(expected = NullPointerException.class)
    public void goShouldThrowNullPointerExceptionWhenProcessingNullPathInArgs() throws Exception {
        // Arrange: The second argument is null. If Lister iterates through arguments
        // to process multiple files, this will cause a NullPointerException when
        // converting the null string to a Path.
        String[] args = {"firstFile.zip", null};
        Lister lister = new Lister(true, args);

        // Act
        lister.go();

        // Assert: A NullPointerException is expected.
    }
}