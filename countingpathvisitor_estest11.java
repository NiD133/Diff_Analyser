package org.apache.commons.io.file;

import org.junit.Test;

import java.io.IOException;

/**
 * This test class contains tests for CountingPathVisitor and its subclasses.
 * The original test was auto-generated and has been refactored for clarity.
 */
public class CountingPathVisitor_ESTestTest11 extends CountingPathVisitor_ESTest_scaffolding {

    /**
     * Tests that preVisitDirectory throws a NullPointerException when given a null path.
     * <p>
     * This behavior is part of the contract of the FileVisitor interface and is tested
     * here through the CleaningPathVisitor subclass, which inherits the method from
     * CountingPathVisitor.
     * </p>
     */
    @Test(expected = NullPointerException.class)
    public void testPreVisitDirectoryThrowsExceptionForNullPath() throws IOException {
        // Arrange: Create a visitor instance.
        // The specific counters and other constructor arguments are not relevant for this test,
        // as we are only verifying the null-check on the path parameter.
        final Counters.PathCounters counters = CountingPathVisitor.withLongCounters().getPathCounters();
        final String[] noPathsToDelete = {};
        final CleaningPathVisitor visitor = new CleaningPathVisitor(counters, null, noPathsToDelete);

        // Act: Call the method with a null path, which is expected to cause an exception.
        // Assert: A NullPointerException is expected, as specified by the @Test annotation.
        visitor.preVisitDirectory(null, null);
    }
}