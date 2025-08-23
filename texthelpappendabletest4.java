package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.params.provider.Arguments.arguments;

import java.io.IOException;
import java.util.Collections;
import java.util.stream.Stream;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.junit.jupiter.params.provider.NullAndEmptySource;

/**
 * Tests for the {@link TextHelpAppendable} class.
 *
 * This refactored test suite improves on the original by enhancing clarity,
 * structure, and maintainability.
 */
@DisplayName("TextHelpAppendable Tests")
public class TextHelpAppendableTest {

    private StringBuilder sb;
    private TextHelpAppendable underTest;

    @BeforeEach
    void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    @Nested
    @DisplayName("indexOfWrap() static method")
    class IndexOfWrapTest {

        @Test
        @DisplayName("should return whitespace position when it exists within the specified width")
        void indexOfWrap_whenWhitespaceExists_shouldReturnWhitespacePosition() {
            // Arrange
            // Text: "Hello World", length 11.
            // Width: 7.
            // We are looking for a wrap position within the first 7 characters ("Hello W").
            // The last whitespace character in this range is the space at index 5.
            final String textWithSpace = "Hello World";
            final int width = 7;
            final int startPos = 0;
            final int expectedPosition = 5; // The index of the space

            // Act
            final int actualPosition = TextHelpAppendable.indexOfWrap(textWithSpace, width, startPos);

            // Assert
            assertEquals(expectedPosition, actualPosition);
        }

        @Test
        @DisplayName("should return position before width when no whitespace exists")
        void indexOfWrap_whenNoWhitespaceExists_shouldReturnPositionBeforeWidth() {
            // Arrange
            // Text: "HelloWorld", length 10.
            // Width: 7.
            // We are looking for a wrap position within the first 7 characters ("HelloWo").
            // Since there is no whitespace, the word must be broken.
            // The wrap should occur after the 6th character (index 6), so the method
            // is expected to return 6 (width - 1).
            final String longWord = "HelloWorld";
            final int width = 7;
            final int startPos = 0;
            final int expectedPosition = 6;

            // Act
            final int actualPosition = TextHelpAppendable.indexOfWrap(longWord, width, startPos);

            // Assert
            assertEquals(expectedPosition, actualPosition);
        }
    }

    @Nested
    @DisplayName("appendHeader() method")
    class AppendHeaderTest {

        @ParameterizedTest(name = "Level {0} header should be underlined with ''{1}''")
        @MethodSource("org.apache.commons.cli.help.TextHelpAppendableTest#headerProvider")
        void appendHeader_withValidLevels_shouldFormatCorrectly(int level, char underlineChar) throws IOException {
            // Arrange
            final String headerText = "Hello World";
            final String underline = String.join("", Collections.nCopies(headerText.length(), String.valueOf(underlineChar)));
            // Expected format is a line with the header, a line with the underline, and a blank line.
            // e.g., for level 1:
            //  Hello World
            //  ===========
            //
            final String expectedOutput = String.format(" %s%n %s%n%n", headerText, underline);

            // Act
            underTest.appendHeader(level, headerText);

            // Assert
            assertEquals(expectedOutput, sb.toString());
        }

        @Test
        @DisplayName("should throw IllegalArgumentException for level 0")
        void appendHeader_withInvalidLevel_shouldThrowException() {
            // Act & Assert
            assertThrows(IllegalArgumentException.class,
                () -> underTest.appendHeader(0, "Some Text"),
                "Level 0 should be an illegal argument.");
            assertEquals(0, sb.length(), "Should not write anything to the buffer on failure.");
        }

        @ParameterizedTest
        @NullAndEmptySource
        @DisplayName("should do nothing for null or empty header text")
        void appendHeader_withNullOrEmptyText_shouldAppendNothing(String headerText) throws IOException {
            // Act
            underTest.appendHeader(1, headerText);

            // Assert
            assertEquals("", sb.toString(), "Should not append anything for null or empty header text.");
        }
    }

    /**
     * Provides arguments for testing various header levels and their expected underline characters.
     * @return A stream of arguments, each containing a level and its corresponding character.
     */
    static Stream<Arguments> headerProvider() {
        return Stream.of(
            arguments(1, '='),
            arguments(2, '%'),
            arguments(3, '+'),
            arguments(4, '_'),
            arguments(5, '_') // Level 5 also uses '_'
        );
    }
}