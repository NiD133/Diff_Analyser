package org.apache.commons.io.file;

import static org.junit.Assert.assertNotNull;

import org.apache.commons.io.file.Counters.PathCounters;
import org.apache.commons.io.filefilter.EmptyFileFilter;
import org.apache.commons.io.filefilter.IOFileFilter;
import org.junit.Test;

/**
 * Tests for the {@link CountingPathVisitor} class, focusing on its constructors.
 */
public class CountingPathVisitorImprovedTest {

    /**
     * Tests that the constructor {@code CountingPathVisitor(PathCounters, PathFilter, PathFilter)}
     * successfully creates an instance when provided with valid arguments.
     */
    @Test
    public void constructorWithCountersAndFiltersShouldCreateInstance() {
        // Arrange: Set up the necessary components for the visitor.
        // We use default counters and a simple, non-null filter.
        final PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        final IOFileFilter filter = EmptyFileFilter.EMPTY;

        // Act: Instantiate the CountingPathVisitor using the constructor under test.
        final CountingPathVisitor visitor = new CountingPathVisitor(pathCounters, filter, filter);

        // Assert: Verify that the visitor object was created successfully.
        // This confirms the constructor does not throw an exception with valid inputs.
        assertNotNull("The visitor should be successfully instantiated.", visitor);
    }
}