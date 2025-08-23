package org.apache.commons.io.file;

import org.junit.Test;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;

/**
 * Contains an improved version of an auto-generated test for {@link CountingPathVisitor}.
 * The original test class structure is preserved for context.
 */
public class CountingPathVisitor_ESTestTest33 extends CountingPathVisitor_ESTest_scaffolding {

    /**
     * Tests that {@link CountingPathVisitor#updateFileCounters(Path, BasicFileAttributes)}
     * throws a NullPointerException when the path argument is null.
     */
    @Test(expected = NullPointerException.class)
    public void updateFileCountersShouldThrowNullPointerExceptionForNullPath() {
        // Arrange: Create a visitor instance.
        final CountingPathVisitor visitor = CountingPathVisitor.withBigIntegerCounters();

        // Act: Call the method under test with a null path. This is expected to throw.
        visitor.updateFileCounters(null, (BasicFileAttributes) null);

        // Assert: The test passes if a NullPointerException is thrown, as declared
        // by the @Test(expected = ...) annotation.
    }
}