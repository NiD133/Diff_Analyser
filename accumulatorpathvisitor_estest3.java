package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.doReturn;
import static org.mockito.Mockito.mock;

import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;
import org.junit.Test;

public class AccumulatorPathVisitor_ESTestTest3 extends AccumulatorPathVisitor_ESTest_scaffolding {

    /**
     * Tests that relativizing a file against a parent path that is identical to the
     * file's own path results in an empty path.
     */
    @Test(timeout = 4000)
    public void testRelativizeFilesWithIdenticalParentPathReturnsEmptyPath() throws Exception {
        // Arrange
        final AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        final Path path = Paths.get("test-dir", "file.txt");
        final BasicFileAttributes mockAttributes = mock(BasicFileAttributes.class);

        // Simulate that the visitor has processed one file by calling updateFileCounters.
        // This adds the path to the visitor's internal file list.
        // The attributes' values are not relevant for this test's logic.
        doReturn(0L).when(mockAttributes).size();
        visitor.updateFileCounters(path, mockAttributes);

        // Act
        // Relativize the collected file list against a parent path that is identical
        // to the file path itself. Sorting is disabled and no comparator is needed.
        final List<Path> relativizedPaths = visitor.relativizeFiles(path, false, null);

        // Assert
        // The result of path.relativize(path) is an empty path.
        // The list should contain a single element, which is that empty path.
        assertEquals(1, relativizedPaths.size());
        assertEquals("The resulting path should be empty.", Paths.get(""), relativizedPaths.get(0));
    }
}