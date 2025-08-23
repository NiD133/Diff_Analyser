package org.jsoup.nodes;

import org.jsoup.nodes.Document.OutputSettings;
import org.junit.Test;

import java.io.IOException;
import java.io.PipedWriter;

import static org.junit.Assert.*;

/**
 * Test suite for the {@link Attribute} class.
 * This test focuses on how the html() method handles IOExceptions.
 */
public class AttributeRefactoredTest {

    /**
     * Tests that when rendering an attribute to an Appendable, if the Appendable
     * throws an IOException, it is caught and wrapped in an unchecked RuntimeException.
     * This behavior simplifies the method signature of `html()`, as callers don't
     * need to handle IOExceptions.
     */
    @Test
    public void htmlToFailingAppendableWrapsIOException() {
        // Arrange
        Attribute attribute = new Attribute("id", "test-id");
        OutputSettings outputSettings = new OutputSettings();

        // Use an unconnected PipedWriter as an Appendable that is guaranteed to
        // throw an IOException upon any write attempt.
        Appendable failingWriter = new PipedWriter();

        // Act & Assert
        try {
            // The deprecated `html(Appendable, ...)` method is being tested here.
            // It internally wraps the Appendable in a QuietAppendable, which
            // is responsible for catching the IOException.
            attribute.html(failingWriter, outputSettings);
            fail("Expected a RuntimeException to be thrown because the underlying writer failed.");
        } catch (RuntimeException e) {
            // Verify that the thrown exception is a RuntimeException...
            assertEquals(RuntimeException.class, e.getClass());
            
            // ...and that its cause is the original IOException.
            Throwable cause = e.getCause();
            assertNotNull("The RuntimeException should have a cause.", cause);
            assertTrue("The cause should be an IOException.", cause instanceof IOException);
            assertEquals("Pipe not connected", cause.getMessage());
        }
    }
}