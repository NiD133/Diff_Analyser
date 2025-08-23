package org.apache.commons.compress.archivers;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import org.junit.Test;

/**
 * Tests for the {@link Lister} command-line application.
 */
public class ListerTest {

    /**
     * Verifies that Lister.main() throws a NullPointerException if the first
     * command-line argument, which represents the archive path, is null.
     */
    @Test
    public void mainShouldThrowNullPointerExceptionForNullArchivePath() {
        // Arrange: Prepare arguments where the first element (the file path) is null.
        final String[] argsWithNullPath = { null };

        try {
            // Act: Call the main method with the invalid arguments.
            Lister.main(argsWithNullPath);

            // Assert: If no exception is thrown, the test should fail.
            fail("Expected a NullPointerException because the archive path cannot be null.");
        } catch (final NullPointerException e) {
            // Assert: The correct exception was caught. This is the expected outcome.
            // The Lister constructor uses Objects.requireNonNull(args[0], "args[0]"),
            // so we can optionally verify the exception message for more precise testing.
            assertEquals("args[0]", e.getMessage());
        } catch (final Exception e) {
            // Assert: Fail if an unexpected exception type is caught.
            fail("Caught an unexpected exception type: " + e.getClass().getName());
        }
    }
}