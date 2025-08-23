package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.Test;

public class AccumulatorPathVisitor_ESTestTest5 extends AccumulatorPathVisitor_ESTest_scaffolding {

    /**
     * Tests that {@link AccumulatorPathVisitor#relativizeDirectories(Path, boolean, java.util.Comparator)}
     * correctly handles the case where the parent path for relativization is the same as a visited directory.
     * The expected result is a list containing a single empty path.
     */
    @Test
    public void testRelativizeDirectoriesWithSelfAsParent() {
        // Arrange
        final AccumulatorPathVisitor visitor = new AccumulatorPathVisitor();
        final Path directoryPath = new MockFile("test-dir").toPath();

        // Simulate a directory visit that adds the path to the visitor's internal list.
        // The exception is required by the method signature but is not used in this test.
        visitor.updateDirCounter(directoryPath, new IOException());

        // Act
        // Relativize the collected directories against the directory path itself.
        // Sorting is disabled, so the comparator is null.
        final List<Path> resultList = visitor.relativizeDirectories(directoryPath, false, null);

        // Assert
        // The result of relativizing a path against itself is an empty path.
        final Path expectedRelativePath = Paths.get("");
        assertEquals(1, resultList.size());
        assertEquals(expectedRelativePath, resultList.get(0));
    }
}