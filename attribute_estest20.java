package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

import static org.junit.Assert.fail;

// The original test class name and hierarchy are preserved.
public class Attribute_ESTestTest20 extends Attribute_ESTest_scaffolding {

    /**
     * Verifies that writing an attribute's HTML to an Appendable that is too small
     * correctly throws a BufferOverflowException.
     */
    @Test(timeout = 4000)
    public void htmlShouldThrowBufferOverflowWhenAppendableIsTooSmall() {
        // Arrange
        // The HTML representation will be `key="value"`, which is 13 characters long.
        Attribute attribute = new Attribute("key", "value");
        Document.OutputSettings outputSettings = new Document.OutputSettings();

        // Create a buffer that is intentionally too small to hold the attribute's HTML.
        final int bufferSize = 10;
        CharBuffer smallBuffer = CharBuffer.allocate(bufferSize);
        QuietAppendable appendable = QuietAppendable.wrap(smallBuffer);

        // Act & Assert
        try {
            attribute.html(appendable, outputSettings);
            fail("Expected a BufferOverflowException because the buffer is too small, but none was thrown.");
        } catch (BufferOverflowException expected) {
            // This is the expected behavior, so the test passes.
        }
    }
}