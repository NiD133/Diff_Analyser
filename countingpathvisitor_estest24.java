package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.assertTrue;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.evosuite.runtime.mock.java.io.MockFile;

// The test class name and inheritance are preserved from the original.
public class CountingPathVisitor_ESTestTest24 extends CountingPathVisitor_ESTest_scaffolding {

    /**
     * Tests that the accept() method returns true for any path when the
     * CountingPathVisitor is created with default filters.
     *
     * This scenario specifically verifies that the method handles null
     * BasicFileAttributes gracefully, as the default filter should
     * not depend on them.
     */
    @Test(timeout = 4000)
    public void acceptShouldReturnTrueForAnyPathWhenUsingDefaultFilters() {
        // Arrange: Create a visitor with default settings.
        // The default visitor is configured to accept all files and directories.
        final CountingPathVisitor visitor = CountingPathVisitor.withBigIntegerCounters();
        final Path path = new MockFile("any/path").toPath();
        final BasicFileAttributes nullAttributes = null;

        // Act: Call the accept method with a path and null attributes.
        final boolean isAccepted = visitor.accept(path, nullAttributes);

        // Assert: Verify that the path was accepted.
        assertTrue("The path should be accepted by the default filter, even with null attributes.", isAccepted);
    }
}