package org.apache.commons.io.output;

import org.junit.Test;
import org.evosuite.runtime.mock.java.io.MockFile;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Tests for {@link XmlStreamWriter}.
 * This class focuses on scenarios related to file-based constructor behavior.
 */
public class XmlStreamWriterTest {

    /**
     * Tests that the constructor throws a FileNotFoundException when the provided
     * File object represents a directory, as it's impossible to write to a directory
     * as if it were a file.
     */
    @Test(expected = FileNotFoundException.class)
    public void constructorWithFile_shouldThrowFileNotFoundException_whenFileIsDirectory() throws FileNotFoundException {
        // A MockFile with an empty path represents the current directory.
        final File directory = new MockFile("");

        // Attempting to construct an XmlStreamWriter with a directory should fail
        // because it internally tries to create a FileOutputStream.
        new XmlStreamWriter(directory, "UTF-8");
    }
}