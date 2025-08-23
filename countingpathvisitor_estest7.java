package org.apache.commons.io.file;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertSame;

import org.apache.commons.io.filefilter.CanWriteFileFilter;
import org.apache.commons.io.filefilter.PathFilter;
import org.junit.Test;

/**
 * Tests for the constructors of {@link CountingPathVisitor}.
 */
public class CountingPathVisitor_ESTestTest7 {

    /**
     * Tests that the CountingPathVisitor can be successfully instantiated
     * using the constructor that accepts custom file and directory filters.
     */
    @Test
    public void constructorWithCustomFiltersShouldCreateInstance() {
        // Arrange: Define custom filters and counters for the visitor.
        // Using long counters, which is the default for the builder.
        final Counters.PathCounters pathCounters = Counters.longCounters();
        
        // A filter that accepts files that cannot be written to.
        final PathFilter fileFilter = CanWriteFileFilter.CANNOT_WRITE;
        
        // A filter that accepts any directory (the default behavior).
        final PathFilter directoryFilter = CountingPathVisitor.defaultDirectoryFilter();

        // Act: Instantiate the CountingPathVisitor with the prepared components.
        final CountingPathVisitor visitor = new CountingPathVisitor(pathCounters, fileFilter, directoryFilter);

        // Assert: Verify that the visitor was created and configured correctly.
        assertNotNull("The visitor instance should not be null.", visitor);
        assertSame("The path counters should be the same instance passed to the constructor.",
                pathCounters, visitor.getPathCounters());
    }
}