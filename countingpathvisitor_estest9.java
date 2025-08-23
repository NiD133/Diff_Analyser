package org.apache.commons.io.file;

import org.junit.Test;
import static org.junit.Assert.assertFalse;

import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.evosuite.runtime.mock.java.io.MockFile;

// The test class structure is kept from the original to show the context.
public class CountingPathVisitor_ESTestTest9 extends CountingPathVisitor_ESTest_scaffolding {

    /**
     * Tests that the accept method returns false when passed null BasicFileAttributes.
     * This is an edge case, as a standard file system walk would always provide
     * non-null attributes for this method call.
     */
    @Test
    public void acceptShouldReturnFalseWhenAttributesAreNull() {
        // Arrange
        // Create a visitor with default (accept-all) filters. The method under test,
        // accept(), is defined in CountingPathVisitor.
        final CountingPathVisitor visitor = CountingPathVisitor.withBigIntegerCounters();
        final Path nonExistentPath = new MockFile("any-file-or-directory").toPath();

        // Act
        // Call the protected 'accept' method directly with null attributes.
        final boolean isAccepted = visitor.accept(nonExistentPath, null);

        // Assert
        assertFalse("The path should not be accepted when BasicFileAttributes are null.", isAccepted);
    }
}