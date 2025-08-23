package org.apache.commons.io.file;

import java.io.IOException;
import org.junit.Test;
import org.evosuite.runtime.mock.java.io.MockIOException;

/**
 * Tests for {@link CountingPathVisitor} and its subclasses.
 * This test case focuses on the behavior of {@link DeletingPathVisitor}.
 */
public class CountingPathVisitorTest {

    /**
     * Tests that DeletingPathVisitor#postVisitDirectory throws a NullPointerException
     * when invoked with a null path.
     */
    @Test(expected = NullPointerException.class)
    public void testPostVisitDirectoryWithNullPathThrowsNPE() throws IOException {
        // Arrange
        // DeletingPathVisitor is a subclass of CountingPathVisitor, used here to test
        // its specific implementation of post-visit directory handling.
        final DeletingPathVisitor visitor = DeletingPathVisitor.withLongCounters();
        final IOException visitException = new MockIOException("Simulated visit error");

        // Act
        // Calling postVisitDirectory with a null path, which is invalid input.
        // The method is expected to fail fast by throwing an exception.
        visitor.postVisitDirectory(null, visitException);

        // Assert
        // The test asserts that a NullPointerException is thrown, which is handled
        // by the 'expected' attribute of the @Test annotation.
    }
}