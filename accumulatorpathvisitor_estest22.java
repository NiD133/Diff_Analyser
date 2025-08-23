package org.apache.commons.io.file;

import org.junit.Test;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

import org.evosuite.runtime.mock.java.io.MockFile;

/**
 * Unit tests for {@link AccumulatorPathVisitor}.
 */
public class AccumulatorPathVisitorTest {

    /**
     * Tests that calling updateFileCounters() with a null BasicFileAttributes argument
     * results in a NullPointerException. This behavior is inherited from the
     * superclass, CountingPathVisitor, which requires attributes to perform its counting logic.
     */
    @Test(expected = NullPointerException.class)
    public void testUpdateFileCountersThrowsExceptionForNullAttributes() {
        // Arrange: Create a visitor and a path to a file.
        final AccumulatorPathVisitor visitor = AccumulatorPathVisitor.withLongCounters();
        final Path filePath = new MockFile("test-file.txt").toPath();
        final BasicFileAttributes nullAttributes = null;

        // Act: Call the method under test with null attributes.
        // Assert: A NullPointerException is expected, as specified by the @Test annotation.
        visitor.updateFileCounters(filePath, nullAttributes);
    }
}