package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 * This class focuses on the static {@code indexOfWrap} method and instance-level property setters.
 */
class TextHelpAppendableTest {

    @Nested
    @DisplayName("indexOfWrap static method")
    class IndexOfWrapTest {

        @Test
        @DisplayName("should return the position of the last whitespace within the given width")
        void indexOfWrap_whenWhitespaceExists_returnsWhitespacePosition() {
            // Arrange
            // Text with a space at index 5. Width is 7, so the space is within the wrap area.
            final String textWithSpace = "Hello World";
            final int wrapWidth = 7;
            final int expectedPosition = textWithSpace.indexOf(' '); // Expected: 5

            // Act
            final int actualPosition = TextHelpAppendable.indexOfWrap(textWithSpace, wrapWidth, 0);

            // Assert
            assertEquals(expectedPosition, actualPosition, "Should find the space character before the wrap width.");
        }

        @Test
        @DisplayName("should return the position of the next word when no whitespace is within the width")
        void indexOfWrap_whenNoWhitespaceExists_returnsNextWordPosition() {
            // Arrange
            // Text without a space in the first 7 characters. The first word "HelloX" has length 6.
            final String textWithoutSpace = "HelloXWorld";
            final int wrapWidth = 7;
            // The expected position is 6, which is the start of the next word "World".
            final int expectedPosition = 6;

            // Act
            final int actualPosition = TextHelpAppendable.indexOfWrap(textWithoutSpace, wrapWidth, 0);

            // Assert
            assertEquals(expectedPosition, actualPosition, "Should wrap after the first word if no space is found.");
        }
    }

    @Nested
    @DisplayName("Indent property")
    class IndentTest {

        private TextHelpAppendable textHelpAppendable;

        @BeforeEach
        void setUp() {
            // Using a StringBuilder, though its content is not used in these specific tests.
            textHelpAppendable = new TextHelpAppendable(new StringBuilder());
        }

        @Test
        @DisplayName("should be initialized with the default value")
        void getIndent_shouldReturnDefaultValueInitially() {
            // Assert
            assertEquals(TextHelpAppendable.DEFAULT_INDENT, textHelpAppendable.getIndent(), "A new instance should have the default indent.");
        }

        @Test
        @DisplayName("should be updatable via setIndent")
        void setIndent_shouldUpdateIndentValue() {
            // Arrange
            final int newIndent = TextHelpAppendable.DEFAULT_INDENT + 5;

            // Act
            textHelpAppendable.setIndent(newIndent);

            // Assert
            assertEquals(newIndent, textHelpAppendable.getIndent(), "getIndent() should return the newly set value.");
        }
    }
}