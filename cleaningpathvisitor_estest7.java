package org.apache.commons.io.file;

import static org.mockito.Mockito.mock;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.BasicFileAttributes;
import org.junit.Test;

/**
 * Tests for {@link CleaningPathVisitor} focusing on security-related exceptions.
 */
public class CleaningPathVisitorSecurityTest {

    /**
     * Tests that visitFile throws a SecurityException when the SecurityManager
     * prevents file deletion. This test relies on the test execution environment
     * (like the one provided by EvoSuite) having a SecurityManager that blocks
     * delete operations.
     *
     * @throws IOException if an I/O error occurs (not expected in this test).
     */
    @Test(timeout = 4000, expected = SecurityException.class)
    public void visitFileShouldThrowSecurityExceptionWhenDeletionIsBlocked() throws IOException {
        // Arrange: Create a visitor that attempts to delete files.
        final CountingPathVisitor cleaningVisitor = CleaningPathVisitor.withBigIntegerCounters();

        // Arrange: Represent the current directory as a file path to attempt deletion.
        // Using an empty path with java.io.File points to the current directory.
        final Path pathToDelete = new java.io.File("").toPath();
        final BasicFileAttributes mockAttributes = mock(BasicFileAttributes.class);

        // Act & Assert: This call attempts to delete the path. The test environment's
        // SecurityManager should intercept this and throw a SecurityException,
        // which is caught and verified by the @Test(expected=...) annotation.
        cleaningVisitor.visitFile(pathToDelete, mockAttributes);
    }
}