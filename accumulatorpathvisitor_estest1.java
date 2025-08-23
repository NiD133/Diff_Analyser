package org.apache.commons.io.file;

import org.apache.commons.io.filefilter.CanExecuteFileFilter;
import org.apache.commons.io.filefilter.CanReadFileFilter;
import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * Tests for {@link AccumulatorPathVisitor}.
 *
 * Note: This class name "AccumulatorPathVisitor_ESTestTest1" appears to be generated.
 * In a real-world scenario, it would be renamed to "AccumulatorPathVisitorTest".
 */
public class AccumulatorPathVisitor_ESTestTest1 extends AccumulatorPathVisitor_ESTest_scaffolding {

    /**
     * Tests that the factory method {@link AccumulatorPathVisitor#withBigIntegerCounters(PathFilter, PathFilter)}
     * successfully creates a non-null instance when provided with valid filters.
     */
    @Test
    public void withBigIntegerCounters_withValidFilters_createsNonNullVisitor() {
        // Arrange: Define filters for files and directories.
        // Using standard, self-explanatory filters to keep the test focused on the visitor's creation.
        final PathFilter fileFilter = CanReadFileFilter.CAN_READ;
        final PathFilter dirFilter = CanExecuteFileFilter.CAN_EXECUTE;

        // Act: Create an AccumulatorPathVisitor using the factory method.
        final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters(fileFilter, dirFilter);

        // Assert: Verify that the factory method returned a valid, non-null instance.
        assertNotNull("The created visitor should not be null.", visitor);
    }
}