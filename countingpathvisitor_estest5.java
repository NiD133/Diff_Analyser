package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.apache.commons.io.file.Counters.PathCounters;
import org.evosuite.runtime.mock.java.io.MockFile;
import org.junit.Test;

/**
 * Tests for {@link CountingPathVisitor}.
 * This test focuses on the visitFile method.
 */
// Note: The original class name "CountingPathVisitor_ESTestTest5" is an artifact
// from a test generation tool. A more conventional name would be "CountingPathVisitorTest".
public class CountingPathVisitor_ESTestTest5 {

    /**
     * Tests that visiting a file correctly increments the file and byte counters
     * and returns {@link FileVisitResult#CONTINUE}.
     */
    @Test
    public void testVisitFileIncrementsCountersAndReturnsContinue() throws IOException {
        // Arrange: Set up the visitor, a mock path, and its attributes.
        final long fileSize = 123L;
        final CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        final Path filePath = new MockFile("test-file.txt").toPath();

        // Mock the file attributes to control the file size for the test.
        final BasicFileAttributes mockAttributes = mock(BasicFileAttributes.class);
        when(mockAttributes.size()).thenReturn(fileSize);

        // Act: Call the method under test.
        final FileVisitResult result = visitor.visitFile(filePath, mockAttributes);

        // Assert: Verify the behavior and the resulting state.
        // 1. The method should return CONTINUE to allow the file tree walk to proceed.
        assertEquals("The visit result should be CONTINUE.", FileVisitResult.CONTINUE, result);

        // 2. The visitor's counters should be updated correctly.
        final PathCounters counters = visitor.getPathCounters();
        assertEquals("File count should be incremented to 1.", 1, counters.getFileCounter().get());
        assertEquals("Byte count should be updated with the file's size.", fileSize, counters.getByteCounter().get().longValue());
        assertEquals("Directory count should remain unchanged.", 0, counters.getDirectoryCounter().get());
    }
}