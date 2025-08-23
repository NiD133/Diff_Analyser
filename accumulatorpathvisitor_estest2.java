package org.apache.commons.io.file;

import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link AccumulatorPathVisitor}.
 */
public class AccumulatorPathVisitorTest {

    @Test
    public void testRelativizeFilesReturnsEmptyListWhenNoFilesVisited() {
        // Arrange: Create a visitor that has not yet visited any files.
        final AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        final Path parentDirectory = Paths.get("some/parent/dir");

        // Act: Attempt to relativize the (empty) list of visited files against a parent directory.
        // Sorting is disabled, and the comparator is null as it would not be used.
        final List<Path> relativizedPaths = visitor.relativizeFiles(parentDirectory, false, null);

        // Assert: The resulting list should be empty because the visitor has not accumulated any files.
        assertTrue("Expected an empty list for a visitor that has not processed any files.", relativizedPaths.isEmpty());
    }
}