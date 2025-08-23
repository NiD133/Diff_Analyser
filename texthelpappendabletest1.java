package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.Queue;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TextHelpAppendable}.
 */
class TextHelpAppendableTest {

    @Test
    @DisplayName("indexOfWrap() should find the correct wrap position based on whitespace")
    @ParameterizedTest(name = "char = ''{0}'', isWhitespace = {1}")
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    void testIndexOfWrapWithAndWithoutWhitespace(final Character c, final boolean isWhitespace) {
        // Arrange
        final String text = String.format("Hello%cWorld", c);
        final int width = 7;
        final int startPos = 0;

        // If whitespace is present within the wrap zone (first 7 chars), the wrap
        // position is the index of the whitespace (5). Otherwise, the test expects
        // the wrap to happen at the last possible character position before the width limit (6).
        final int expectedWrapPos = isWhitespace ? 5 : 6;
        final String scenario = isWhitespace ? "with whitespace" : "without whitespace";

        // Act
        final int actualWrapPos = TextHelpAppendable.indexOfWrap(text, width, startPos);

        // Assert
        assertEquals(expectedWrapPos, actualWrapPos, "Incorrect wrap position for scenario: " + scenario);
    }

    @Nested
    @DisplayName("makeColumnQueue() tests")
    class MakeColumnQueueTest {

        private TextHelpAppendable underTest;

        @BeforeEach
        void setUp() {
            // The StringBuilder is not used by makeColumnQueue, so it's not needed here.
            underTest = new TextHelpAppendable(new StringBuilder());
        }

        private final String TEXT_TO_WRAP = "The quick brown fox jumps over the lazy dog";

        @Test
        @DisplayName("should format text with left alignment")
        void testMakeColumnQueueLeftAligned() {
            // Arrange
            final TextStyle style = TextStyle.builder()
                    .setMaxWidth(10)
                    .setAlignment(TextStyle.Alignment.LEFT)
                    .get();

            final Queue<String> expected = new LinkedList<>(Arrays.asList(
                    "The quick ",
                    "brown fox ",
                    "jumps over",
                    "the lazy  ",
                    "dog       "
            ));

            // Act
            final Queue<String> actual = underTest.makeColumnQueue(TEXT_TO_WRAP, style);

            // Assert
            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("should format text with right alignment")
        void testMakeColumnQueueRightAligned() {
            // Arrange
            final TextStyle style = TextStyle.builder()
                    .setMaxWidth(10)
                    .setAlignment(TextStyle.Alignment.RIGHT)
                    .get();

            final Queue<String> expected = new LinkedList<>(Arrays.asList(
                    " The quick",
                    " brown fox",
                    "jumps over",
                    "  the lazy",
                    "       dog"
            ));

            // Act
            final Queue<String> actual = underTest.makeColumnQueue(TEXT_TO_WRAP, style);

            // Assert
            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("should format text with center alignment")
        void testMakeColumnQueueCenterAligned() {
            // Arrange
            final TextStyle style = TextStyle.builder()
                    .setMaxWidth(10)
                    .setAlignment(TextStyle.Alignment.CENTER)
                    .get();

            final Queue<String> expected = new LinkedList<>(Arrays.asList(
                    "The quick ",
                    "brown fox ",
                    "jumps over",
                    " the lazy ",
                    "   dog    "
            ));

            // Act
            final Queue<String> actual = underTest.makeColumnQueue(TEXT_TO_WRAP, style);

            // Assert
            assertEquals(expected, actual);
        }

        @Test
        @DisplayName("should format text with right alignment, padding, and indent")
        void testMakeColumnQueueRightAlignedWithPaddingAndIndent() {
            // Arrange
            final TextStyle style = TextStyle.builder()
                    .setMaxWidth(10)
                    .setLeftPad(5)
                    .setIndent(2)
                    .setAlignment(TextStyle.Alignment.RIGHT)
                    .get();

            final Queue<String> expected = new LinkedList<>(Arrays.asList(
                    "      The quick",
                    "          brown",
                    "            fox",
                    "          jumps",
                    "       over the",
                    "       lazy dog"
            ));

            // Act
            final Queue<String> actual = underTest.makeColumnQueue(TEXT_TO_WRAP, style);

            // Assert
            assertEquals(expected, actual);
        }
    }
}