package org.apache.commons.io.file;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.Test;

/**
 * Tests for the constructor of {@link CleaningPathVisitor}.
 */
public class CleaningPathVisitor_ESTestTest3 {

    /**
     * Tests that the CleaningPathVisitor constructor throws a NullPointerException
     * if the 'skip' var-args array contains a null element.
     * The constructor internally sorts this array, which fails on nulls.
     */
    @Test(expected = NullPointerException.class)
    public void testConstructorThrowsNpeWhenSkipArrayContainsNull() {
        // Arrange: Set up the arguments for the constructor.
        final PathCounters counters = CountingPathVisitor.defaultPathCounters();
        final DeleteOption[] deleteOptions = {}; // Use a valid, empty array to isolate the cause.
        final String[] skipPathsWithNull = {"path/to/keep", null, "another/path"};

        // Act & Assert: Instantiating the visitor with a null in the skip array
        // is expected to throw a NullPointerException. The @Test annotation handles the assertion.
        new CleaningPathVisitor(counters, deleteOptions, skipPathsWithNull);
    }
}