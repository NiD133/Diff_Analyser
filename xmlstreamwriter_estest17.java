package org.apache.commons.io.output;

import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertThrows;

import java.io.File;
import java.io.FileNotFoundException;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.Test;

// The original test class name is retained for context.
public class XmlStreamWriter_ESTestTest17 extends XmlStreamWriter_ESTest_scaffolding {

    /**
     * Tests that creating an {@link XmlStreamWriter} for a file that is actually a directory
     * results in a {@link FileNotFoundException}.
     */
    @Test
    public void creatingWriterForDirectoryThrowsFileNotFoundException() {
        // Arrange: Create a mock File that represents a directory.
        // The constructor for XmlStreamWriter is expected to fail because it cannot
        // open a FileOutputStream on a directory.
        final File directory = new MockFile("", "");

        // Act & Assert: Expect a FileNotFoundException when constructing the writer.
        final FileNotFoundException e = assertThrows(
            FileNotFoundException.class,
            () -> new XmlStreamWriter(directory)
        );

        // The original auto-generated test verified that the mock environment throws
        // an exception with a null message. This assertion is kept for equivalence.
        assertNull("The exception message from the mock environment should be null.", e.getMessage());
    }
}