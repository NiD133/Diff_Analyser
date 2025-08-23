package org.apache.commons.io.file;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.Test;

/**
 * Tests for the {@link CountingPathVisitor} class, focusing on its internal counter update logic.
 */
public class CountingPathVisitorTest {

    /**
     * Tests that the protected method {@code updateFileCounters} correctly increments the file count
     * and the total byte size when visiting a file.
     */
    @Test
    public void testUpdateFileCountersIncrementsFileAndByteCounts() {
        // Arrange
        final long fileSize = 123L;
        final CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        final Path mockPath = mock(Path.class);
        final BasicFileAttributes mockAttributes = mock(BasicFileAttributes.class);

        // Stub the size() method to return a specific file size.
        when(mockAttributes.size()).thenReturn(fileSize);

        // Pre-check: Ensure counters are initially zero.
        PathCounters initialCounters = visitor.getPathCounters();
        assertEquals("Initial file count should be zero.", 0, initialCounters.getFileCounter().get());
        assertEquals("Initial byte count should be zero.", 0, initialCounters.getByteCounter().get());

        // Act
        // Call the protected method under test. This is possible because the test
        // is in the same package as the class being tested.
        visitor.updateFileCounters(mockPath, mockAttributes);

        // Assert
        PathCounters finalCounters = visitor.getPathCounters();
        assertEquals("File count should be incremented to 1.", 1, finalCounters.getFileCounter().get());
        assertEquals("Byte count should be updated to the file's size.", fileSize, finalCounters.getByteCounter().get());
        assertEquals("Directory count should remain unchanged.", 0, finalCounters.getDirectoryCounter().get());
    }
}