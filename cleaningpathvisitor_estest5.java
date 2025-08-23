package org.apache.commons.io.file;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import static org.mockito.Mockito.mock;

// The test class name and scaffolding are kept from the original for context.
// In a real-world scenario, these might also be renamed for clarity.
public class CleaningPathVisitor_ESTestTest5 extends CleaningPathVisitor_ESTest_scaffolding {

    /**
     * Tests that calling visitFile on the root directory throws an IOException.
     * The underlying file system operations are not designed to delete the root path ("/"),
     * and the visitor should propagate this failure.
     */
    @Test(expected = IOException.class)
    public void visitFile_whenPathIsRootDirectory_throwsIOException() throws IOException {
        // Arrange
        final DeleteOption[] deleteOptions = { StandardDeleteOption.OVERRIDE_READ_ONLY };
        final String[] noExclusions = {};
        final CleaningPathVisitor visitor = new CleaningPathVisitor((PathCounters) null, deleteOptions, noExclusions);

        // Using a mock file to represent the root directory path ("/")
        final Path rootPath = new org.evosuite.runtime.mock.java.io.MockFile("", "").toPath();
        final BasicFileAttributes mockAttributes = mock(BasicFileAttributes.class);

        // Act & Assert
        // This call is expected to fail and throw an IOException because PathUtils.delete()
        // cannot operate on the root directory. The assertion is handled by the
        // @Test(expected = IOException.class) annotation.
        visitor.visitFile(rootPath, mockAttributes);
    }
}