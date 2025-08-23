package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;
import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

/**
 * Test suite for exception handling in the {@link Entities} class.
 */
public class Entities_ESTestTest15 {

    /**
     * Verifies that Entities.escape throws a BufferOverflowException when the
     * destination Appendable has insufficient capacity to hold the escaped string.
     */
    @Test(expected = BufferOverflowException.class)
    public void escapeToAppendableWithInsufficientCapacityThrowsBufferOverflow() {
        // Arrange: Create an Appendable with a small, fixed capacity of 2 characters.
        CharBuffer smallBuffer = CharBuffer.allocate(2);
        QuietAppendable appendable = QuietAppendable.wrap(smallBuffer);

        // The input string is intentionally longer than the buffer's capacity.
        String longInputString = "This will overflow";
        Document.OutputSettings outputSettings = new Document.OutputSettings();
        
        // The 'options' parameter (2) corresponds to Entities.ForAttribute. Its specific
        // value is not critical for this test, as any escape attempt will overflow.
        int forAttributeOptions = 2;

        // Act & Assert: Attempting to escape the long string into the small appendable
        // is expected to throw a BufferOverflowException, which is handled by the
        // @Test(expected=...) annotation.
        Entities.escape(appendable, longInputString, outputSettings, forAttributeOptions);
    }
}