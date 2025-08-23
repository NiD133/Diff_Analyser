package org.apache.commons.cli.help;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

/**
 * Contains tests for the static utility method {@link TextHelpAppendable#indexOfWrap(CharSequence, int, int)}.
 *
 * Note: The original test class name 'TextHelpAppendable_ESTestTest11' suggests it was
 * auto-generated. This refactored version focuses on improving the clarity and maintainability
 * of the test method.
 */
public class TextHelpAppendable_ESTestTest11 {

    /**
     * Verifies that indexOfWrap returns the length of the text when the starting
     * position for the search is out of bounds (i.e., greater than the text's length).
     */
    @Test
    public void indexOfWrapShouldReturnTextLengthWhenStartIsOutOfBounds() {
        // Arrange
        // Use an empty text to test the boundary condition.
        final CharSequence emptyText = new StringBuffer();
        final int width = 74; // This value is not relevant for this specific scenario but is required by the method signature.
        final int startPosition = 100; // Any position > emptyText.length() (which is 0) serves to test the out-of-bounds case.

        // Act
        final int wrapIndex = TextHelpAppendable.indexOfWrap(emptyText, width, startPosition);

        // Assert
        // The method is expected to return the length of the text, which is 0 for an empty text.
        assertEquals("The wrap index should be the length of the text for an out-of-bounds start position.",
                emptyText.length(), wrapIndex);
    }
}