package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.Test;

/**
 * Contains tests for the {@link CleaningPathVisitor} class.
 * This specific test focuses on the behavior of the preVisitDirectory method.
 */
public class CleaningPathVisitorTest { // Renamed for clarity

    /**
     * Tests that preVisitDirectory returns SKIP_SUBTREE when the directory's
     * name is present in the configured skip list. This prevents the visitor
     * from traversing into directories that are meant to be preserved.
     */
    @Test
    public void preVisitDirectoryShouldSkipSubtreeWhenDirectoryNameIsInSkipList() throws IOException {
        // --- Arrange ---

        // Define a directory name that the visitor should skip.
        final String directoryNameToSkip = "do-not-clean-this-dir";

        // Create a list of file/directory names to skip. The visitor should ignore any
        // path whose file name component matches an entry in this list.
        final String[] skipList = {"some-other-file.txt", directoryNameToSkip, "another-dir-to-keep"};

        // The PathCounters are required for the constructor but are not relevant to this
        // test's logic. Using no-op counters simplifies the setup.
        final CleaningPathVisitor visitor = new CleaningPathVisitor(Counters.noopPathCounters(), skipList);

        // Create a Path object representing the directory to be visited.
        // The full path doesn't matter, as the visitor only checks the file name.
        final Path directoryToVisit = Paths.get("parent/directory", directoryNameToSkip);
        final BasicFileAttributes mockAttributes = mock(BasicFileAttributes.class);

        // --- Act ---

        // Attempt to visit the directory that is configured to be skipped.
        final FileVisitResult result = visitor.preVisitDirectory(directoryToVisit, mockAttributes);

        // --- Assert ---

        // The visitor should return SKIP_SUBTREE, indicating that this directory
        // and all its contents should be skipped.
        assertEquals("Expected to skip the subtree of a directory in the skip list",
                FileVisitResult.SKIP_SUBTREE, result);
    }
}