package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.io.IOException;
import java.nio.file.Path;
import java.util.List;

import org.evosuite.runtime.mock.java.io.MockFile;
import org.evosuite.runtime.mock.java.io.MockIOException;
import org.junit.Test;

/**
 * Contains improved tests for the {@link AccumulatorPathVisitor} class.
 */
public class AccumulatorPathVisitorRefactoredTest {

    /**
     * Tests that a directory is added to the accumulator's list even when
     * the visit fails with an IOException.
     *
     * <p>The {@code updateDirCounter} method is called by a file-walking
     * mechanism when an error occurs after visiting a directory's entries.
     * This test simulates that specific failure scenario to ensure the path
     * is still recorded.</p>
     */
    @Test(timeout = 4000)
    public void testDirectoryIsAccumulatedOnVisitFailure() {
        // Arrange: Set up the visitor, a path for a directory, and a simulated I/O exception.
        final AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        final Path directoryPath = new MockFile("test-dir").toPath();
        final IOException simulatedException = new MockIOException("Simulated disk failure");

        // Act: Simulate a failed directory visit by directly invoking the responsible method.
        // This is the method the file walker would call upon encountering an IOException
        // for a directory.
        visitor.updateDirCounter(directoryPath, simulatedException);

        // Assert: Verify that the directory path was still recorded in the visitor's list.
        final List<Path> accumulatedDirs = visitor.getDirList();
        assertEquals("The directory list should contain exactly one entry.", 1, accumulatedDirs.size());
        assertTrue("The list should contain the path of the directory that failed to be visited.",
            accumulatedDirs.contains(directoryPath));
    }
}