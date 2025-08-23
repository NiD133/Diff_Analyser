package org.apache.commons.io.file;

import org.apache.commons.io.file.Counters.PathCounters;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Tests for {@link CleaningPathVisitor}.
 */
public class CleaningPathVisitorTest {

    /**
     * Tests that preVisitDirectory throws a NullPointerException when the directory path is null.
     */
    @Test(expected = NullPointerException.class)
    public void preVisitDirectory_withNullPath_throwsNullPointerException() throws IOException {
        // Arrange: Create a visitor with valid, empty dependencies.
        final PathCounters pathCounters = new CountingPathVisitor.Builder().getPathCounters();
        final String[] noExclusions = {}; // Use an empty array for clarity and robustness.
        final CleaningPathVisitor cleaningVisitor = new CleaningPathVisitor(pathCounters, noExclusions);

        // Act: Call the method with a null path, which is expected to fail.
        // The BasicFileAttributes argument can also be null for this test case.
        cleaningVisitor.preVisitDirectory((Path) null, (BasicFileAttributes) null);

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // by the @Test(expected=...) annotation.
    }
}