package org.apache.commons.io.file;

import org.junit.Test;

import java.nio.file.Path;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockIOException;

/**
 * Tests for {@link AccumulatorPathVisitor}.
 */
public class AccumulatorPathVisitorTest {

    /**
     * Tests that relativizeDirectories() throws an IllegalArgumentException when trying
     * to relativize a stored absolute path against a new relative path. This is because
     * the underlying Path.relativize() method requires both paths to be of the same
     * type (either both absolute or both relative).
     */
    @Test(expected = IllegalArgumentException.class)
    public void testRelativizeDirectoriesThrowsExceptionForMismatchedPathTypes() {
        // Arrange: Create a visitor and add an absolute path to its directory list.
        final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        
        // On Unix-like systems, new MockFile("", "") creates a Path for the root ("/").
        // This is an absolute path.
        final Path absolutePath = new MockFile("", "").toPath(); 
        
        // The specific exception doesn't matter; it's just needed to add the path to the list.
        visitor.updateDirCounter(absolutePath, new MockIOException("dummy exception"));

        // This path is relative.
        final Path relativePath = new MockFile("some/relative/dir").toPath();

        // Act & Assert: Attempt to relativize the stored absolute path against the new
        // relative path. This is expected to throw an IllegalArgumentException.
        visitor.relativizeDirectories(relativePath, false, null);
    }
}