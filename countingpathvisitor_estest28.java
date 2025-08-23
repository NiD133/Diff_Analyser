package org.apache.commons.io.file;

import org.junit.Test;

import static org.junit.Assert.assertNotNull;

/**
 * This test case verifies the creation of an {@link AccumulatorPathVisitor}.
 * It specifically checks that the visitor can be instantiated using the default
 * filter provided by {@link CountingPathVisitor}.
 */
public class CountingPathVisitor_ESTestTest28 {

    /**
     * Tests that an AccumulatorPathVisitor can be successfully created using the
     * default directory filter from CountingPathVisitor. This acts as a basic
     * smoke test to ensure the factory method works as expected with this input.
     */
    @Test
    public void testAccumulatorPathVisitorCreationWithDefaultFilter() {
        // Arrange: Get the default filter used for directories in CountingPathVisitor.
        // This filter is being used here for both files and directories.
        final PathFilter defaultDirectoryFilter = CountingPathVisitor.defaultDirectoryFilter();

        // Act: Create an AccumulatorPathVisitor using the retrieved filter.
        final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withBigIntegerCounters(
            defaultDirectoryFilter,
            defaultDirectoryFilter
        );

        // Assert: The factory method should successfully return a non-null instance.
        assertNotNull("The created AccumulatorPathVisitor should not be null.", visitor);
    }
}