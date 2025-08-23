package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.Test;

/**
 * Tests for {@link CleaningPathVisitor}.
 */
public class CleaningPathVisitorTest {

    /**
     * Tests that preVisitDirectory returns CONTINUE even when given null inputs.
     * This ensures the visitor is robust and does not throw a NullPointerException
     * for these edge cases, allowing the file walk to proceed as expected.
     */
    @Test
    public void preVisitDirectoryWithNullInputsShouldReturnContinue() throws IOException {
        // Arrange: Create a visitor instance. The specific counter type is not
        // relevant for this test's logic.
        final CountingPathVisitor visitor = CleaningPathVisitor.withBigIntegerCounters();
        final Path nullDirectory = null;
        final BasicFileAttributes nullAttributes = null;

        // Act: Call the method under test with null arguments.
        final FileVisitResult result = visitor.preVisitDirectory(nullDirectory, nullAttributes);

        // Assert: Verify that the visitor signals to continue the file walk.
        assertEquals(FileVisitResult.CONTINUE, result);
    }
}