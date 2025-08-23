package com.itextpdf.text.pdf;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

// The test runner and scaffolding are preserved as they are part of the existing test infrastructure.
import org.junit.runner.RunWith;
import org.evosuite.runtime.EvoRunner;

/**
 * Contains tests for the {@link MappedRandomAccessFile} constructor.
 */
@RunWith(EvoRunner.class) // This annotation is kept to match the project's test setup.
public class MappedRandomAccessFile_ESTestTest19 extends MappedRandomAccessFile_ESTest_scaffolding {

    /**
     * Verifies that the constructor throws a FileNotFoundException when
     * attempting to open a file that does not exist.
     */
    @Test(expected = FileNotFoundException.class, timeout = 4000)
    public void constructor_whenFileDoesNotExist_throwsFileNotFoundException() throws IOException {
        // Arrange: Define a path for a file that is guaranteed not to exist and a valid mode.
        final String nonExistentFileName = "path/to/a/definitely/non-existent-file.dat";
        final String readWriteMode = "rw";

        // Act & Assert: Attempting to create a MappedRandomAccessFile for the non-existent file
        // should throw a FileNotFoundException. The @Test(expected=...) annotation handles the assertion.
        new MappedRandomAccessFile(nonExistentFileName, readWriteMode);
    }
}