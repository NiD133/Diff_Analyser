package org.apache.commons.io.file;

import static org.junit.Assert.assertSame;
import static org.junit.Assert.fail;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import org.junit.Test;

/**
 * Tests for {@link CountingPathVisitor}.
 */
public class CountingPathVisitorTest {

    /**
     * Tests that postVisitDirectory re-throws the IOException it receives,
     * which is the expected behavior when a directory visit is terminated by an error.
     */
    @Test
    public void testPostVisitDirectoryRethrowsSuppliedException() {
        // Arrange
        // Create a standard visitor instance.
        final CountingPathVisitor visitor = CountingPathVisitor.withLongCounters();
        
        // A dummy path for the directory parameter. It does not need to exist on the file system.
        final Path dummyDirectory = Paths.get("any/directory");
        
        // The exception simulating an I/O error that occurred while visiting the directory.
        final IOException inputException = new IOException("Simulated I/O error");

        // Act & Assert
        // The postVisitDirectory method should re-throw the exact exception it receives.
        try {
            visitor.postVisitDirectory(dummyDirectory, inputException);
            fail("Expected an IOException to be thrown.");
        } catch (final IOException thrownException) {
            // Verify that the thrown exception is the same instance we passed in.
            assertSame("The method should re-throw the exact same exception instance.", 
                         inputException, thrownException);
        }
    }
}