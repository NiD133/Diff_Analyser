package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedWriter;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

/**
 * Test suite for the {@link Attribute} class.
 */
public class AttributeTest {

    /**
     * Verifies that the static {@code Attribute.html()} method wraps any {@link IOException}
     * from the provided {@link Appendable} into an unchecked {@link RuntimeException}.
     * This ensures that callers don't have to handle checked IOExceptions for a method
     * that is primarily used for string building.
     */
    @Test
    public void htmlMethodShouldWrapIOExceptionInRuntimeException() {
        // Arrange: Create an Appendable that is guaranteed to throw an IOException on write.
        // An unconnected PipedWriter is a standard way to achieve this for testing.
        PipedWriter failingWriter = new PipedWriter();
        OutputSettings settings = new OutputSettings();

        // Act & Assert
        try {
            // The html() method uses an internal QuietAppendable that catches IOExceptions
            // and re-throws them as RuntimeExceptions.
            Attribute.html("key", "value", failingWriter, settings);
            fail("A RuntimeException was expected but not thrown.");
        } catch (RuntimeException e) {
            // Verify that the thrown exception is a RuntimeException
            // and that its cause is the original IOException.
            Throwable cause = e.getCause();
            assertNotNull("The RuntimeException should have a cause.", cause);
            assertTrue("The cause of the RuntimeException should be an IOException.", cause instanceof IOException);
            assertEquals("Pipe not connected", cause.getMessage());
        } catch (IOException e) {
            // This block should not be reached. It's included to explicitly fail the test
            // if an unwrapped IOException is thrown, which would violate the method's contract.
            fail("The IOException should have been wrapped in a RuntimeException.");
        }
    }
}