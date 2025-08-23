package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

/**
 * Tests for {@link TextHelpAppendable}.
 */
class TextHelpAppendableTest {

    private StringBuilder sb;
    private TextHelpAppendable underTest;

    @BeforeEach
    void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    @DisplayName("indexOfWrap should return the position of the last whitespace character within the search width")
    @ParameterizedTest(name = "For whitespace char ''{0}''")
    @ValueSource(chars = {' ', '\t', '\n'})
    void indexOfWrap_whenWhitespaceIsPresent_returnsItsPosition(final char whitespaceChar) {
        // Arrange
        final String text = "Hello" + whitespaceChar + "World"; // Whitespace is at index 5
        final int searchWidth = 7;
        final int startPosition = 0;
        final int expectedPosition = 5;

        // Act
        final int actualPosition = TextHelpAppendable.indexOfWrap(text, searchWidth, startPosition);

        // Assert
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    @DisplayName("indexOfWrap should return a position just within the width limit when no whitespace is found")
    void indexOfWrap_whenNoWhitespaceIsPresent_returnsPositionBeforeWidthLimit() {
        // Arrange
        final String text = "HelloWorld";
        final int searchWidth = 7;
        final int startPosition = 0;
        // The expected behavior is non-obvious: for a width of 7, it returns 6.
        // This suggests it finds the last valid character index before the text would wrap.
        final int expectedPosition = 6;

        // Act
        final int actualPosition = TextHelpAppendable.indexOfWrap(text, searchWidth, startPosition);

        // Assert
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    @DisplayName("append(char) should correctly write a supplementary character to the buffer")
    void append_withSupplementaryCharacter_writesToBuffer() throws IOException {
        // Arrange
        // U+1F44D is the "thumbs up" emoji (👍), a supplementary character.
        final int thumbsUpCodepoint = 0x1F44D;
        final String expectedString = new String(Character.toChars(thumbsUpCodepoint));

        // Act
        underTest.append(thumbsUpCodepoint);

        // Assert
        // A supplementary character is represented by a surrogate pair in a Java String (UTF-16),
        // so its length is 2, not 1.
        assertEquals(2, sb.length());
        assertEquals(expectedString, sb.toString());
    }

    @Test
    @DisplayName("append(CharSequence) should write the full sequence to the buffer")
    void append_withCharSequence_writesToBuffer() throws IOException {
        // Arrange
        final String text = "Hello";

        // Act
        underTest.append(text);

        // Assert
        assertEquals("Hello", sb.toString());
    }
}