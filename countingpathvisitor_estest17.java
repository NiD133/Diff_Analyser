package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;

import java.nio.file.Path;
import org.apache.commons.io.file.Counters.PathCounters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.Test;

/**
 * Tests for {@link CountingPathVisitor}.
 * This test focuses on the behavior of the updateDirCounter method.
 */
public class CountingPathVisitor_ESTestTest17 extends CountingPathVisitor_ESTest_scaffolding {

    /**
     * Tests that calling updateDirCounter with a null IOException correctly increments the directory count.
     * This simulates a successful directory visit.
     */
    @Test
    public void updateDirCounterWithNullIOExceptionIncrementsDirectoryCount() {
        // Arrange
        // Create a visitor with standard long counters.
        final CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        final PathCounters counters = visitor.getPathCounters();
        final Path directoryPath = new MockFile("test-directory").toPath();

        // Verify the initial state before the action.
        assertEquals("Directory count should be zero initially.", 0, counters.getDirectoryCounter());

        // Act
        // Call the method under test, simulating a successful post-visit of a directory.
        visitor.updateDirCounter(directoryPath, null);

        // Assert
        // Verify that the directory counter was incremented and other counters remain unchanged.
        assertEquals("Directory count should be incremented after a successful visit.", 1, counters.getDirectoryCounter());
        assertEquals("File count should remain unchanged.", 0, counters.getFileCounter());
        assertEquals("Byte count should remain unchanged.", 0, counters.getByteCounter().longValue());
    }
}