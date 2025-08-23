package org.apache.commons.io.file;

import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.util.List;
import org.junit.Test;

/**
 * Tests for {@link AccumulatorPathVisitor}.
 * This class focuses on a specific test case from a larger generated suite.
 */
public class AccumulatorPathVisitor_ESTestTest19 extends AccumulatorPathVisitor_ESTest_scaffolding {

    /**
     * Tests that a newly instantiated AccumulatorPathVisitor has an empty directory list.
     */
    @Test
    public void getDirListShouldReturnEmptyListForNewVisitor() {
        // Arrange: Create a new AccumulatorPathVisitor.
        // No file system interaction is needed for this test.
        final Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        final AccumulatorPathVisitor visitor = new AccumulatorPathVisitor(pathCounters);

        // Act: Get the directory list from the new visitor before any traversal.
        final List<Path> dirList = visitor.getDirList();

        // Assert: The list should be empty.
        assertTrue("The directory list of a newly created visitor should be empty.", dirList.isEmpty());
    }
}