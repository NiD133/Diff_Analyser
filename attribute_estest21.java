package org.jsoup.nodes;

import org.jsoup.internal.QuietAppendable;
import org.junit.Test;

import java.nio.BufferOverflowException;
import java.nio.CharBuffer;

/**
 * Tests for the static {@link Attribute#html(String, String, QuietAppendable, Document.OutputSettings)} method.
 */
public class AttributeHtmlMethodTest {

    /**
     * Verifies that the html() method throws a BufferOverflowException when the provided
     * Appendable has insufficient capacity to hold the attribute's string representation.
     */
    @Test(expected = BufferOverflowException.class)
    public void htmlThrowsBufferOverflowWhenAppendableIsTooSmall() {
        // Arrange: Create an Appendable with a capacity that is too small for the attribute's HTML.
        // The output for an attribute is ` key="value"`, which requires more than 4 characters.
        final int bufferCapacity = 4;
        CharBuffer smallCharBuffer = CharBuffer.allocate(bufferCapacity);
        QuietAppendable smallAppendable = QuietAppendable.wrap(smallCharBuffer);

        Document.OutputSettings outputSettings = new Document.OutputSettings();
        String attributeKey = "checked";
        String attributeValue = "some-long-value";

        // Act: Attempt to write the attribute's HTML to the small appendable.
        // This should trigger a BufferOverflowException because the output string is larger than the buffer's capacity.
        Attribute.html(attributeKey, attributeValue, smallAppendable, outputSettings);

        // Assert: The test passes if a BufferOverflowException is thrown, as declared in the @Test annotation.
    }
}