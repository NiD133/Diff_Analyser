package org.apache.commons.cli.help;

import org.junit.Test;
import java.nio.CharBuffer;

/**
 * Tests for the {@link TextHelpAppendable} class, focusing on the {@code indexOfWrap} method.
 */
public class TextHelpAppendableTest {

    /**
     * Tests that indexOfWrap throws an IndexOutOfBoundsException when the starting position is negative.
     * The method should reject invalid negative indices before any other processing.
     */
    @Test(expected = IndexOutOfBoundsException.class)
    public void indexOfWrap_withNegativeStartPosition_throwsIndexOutOfBoundsException() {
        // Arrange: Create a sample CharSequence and define an invalid negative start position.
        // The actual content and width are irrelevant as the start position check should fail first.
        final CharSequence text = CharBuffer.allocate(80);
        final int width = 74;
        final int negativeStartPosition = -1;

        // Act: Call the method with the invalid start position.
        // An IndexOutOfBoundsException is expected, which is handled by the @Test annotation.
        TextHelpAppendable.indexOfWrap(text, width, negativeStartPosition);

        // Assert: The test passes if the expected exception is thrown.
    }
}