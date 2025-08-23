package org.apache.commons.io.file;

import org.junit.Test;

/**
 * Tests for {@link CleaningPathVisitor}.
 */
public class CleaningPathVisitor_ESTestTest4 extends CleaningPathVisitor_ESTest_scaffolding {

    /**
     * Tests that the CleaningPathVisitor constructor throws a NullPointerException
     * if the 'skip' array contains a null element. The constructor attempts to sort
     * this array, which fails if any element is null.
     */
    @Test(expected = NullPointerException.class)
    public void constructorShouldThrowNullPointerExceptionWhenSkipArrayContainsNull() {
        // Arrange: Create path counters and a 'skip' array with a null element.
        final Counters.PathCounters pathCounters = CountingPathVisitor.defaultPathCounters();
        final String[] skipPathsWithNull = {"file-to-keep.txt", null};

        // Act: Attempt to create a CleaningPathVisitor with the invalid array.
        // This call is expected to throw a NullPointerException.
        new CleaningPathVisitor(pathCounters, skipPathsWithNull);

        // Assert: The test passes if the expected NullPointerException is thrown.
        // This is handled by the @Test(expected = ...) annotation.
    }
}