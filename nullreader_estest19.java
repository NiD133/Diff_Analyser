package org.apache.commons.io.input;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.fail;

import java.io.IOException;
import org.junit.Test;

/**
 * Contains tests for the {@link NullReader} class.
 */
public class NullReaderTest {

    /**
     * Verifies that calling reset() before a position has been marked throws an IOException.
     * The Reader contract specifies that reset() should throw an IOException if no
     * mark has been set.
     */
    @Test
    public void testResetShouldThrowIOExceptionWhenNotMarked() {
        // Arrange: Create a NullReader that supports marking. The size is not relevant.
        final NullReader reader = new NullReader(100L, true, false);

        // Act & Assert
        try {
            reader.reset();
            fail("Expected an IOException to be thrown because reset() was called before mark().");
        } catch (final IOException e) {
            // Assert that the exception has the expected message.
            final String expectedMessage = "No position has been marked";
            assertEquals(expectedMessage, e.getMessage());
        }
    }
}