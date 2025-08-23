package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TextHelpAppendable} focusing on text formatting and wrapping.
 */
// The class name has been corrected from "TestTest7" to "Test7".
public class TextHelpAppendableTest7 {

    private StringBuilder sb;
    private TextHelpAppendable underTest;

    @BeforeEach
    void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    @DisplayName("indexOfWrap() should find the position of a break character or the end of the line")
    @ParameterizedTest(name = "For char = ''{0}'', isBreakChar = {1}")
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    void indexOfWrap_shouldFindBreakPositionOrLineEnd(final Character c, final boolean isBreakChar) {
        // Arrange
        // The test string has a variable character at index 5.
        final String text = String.format("Hello%cWorld", c);
        final int wrapWidth = 7;
        final int startPosition = 0;

        // If 'c' is a break character (like whitespace), the wrap should happen at its position (index 5).
        // If no break char is found within the line limit (width=7), the method returns
        // the last valid index within that limit, which is `startPosition + wrapWidth - 1 = 6`.
        final int expectedPosition = isBreakChar ? 5 : 6;

        // Act
        final int actualPosition = TextHelpAppendable.indexOfWrap(text, wrapWidth, startPosition);

        // Assert
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    @DisplayName("appendParagraphFormat() should format text with arguments and append it")
    void appendParagraphFormat_shouldFormatAndAppendText() throws IOException {
        // Arrange
        final String format = "Hello %s World %,d";
        // The method is expected to append the formatted string followed by a blank line.
        final List<String> expectedLines = List.of(" Hello Joe World 309", "");

        // Act
        underTest.appendParagraphFormat(format, "Joe", 309);
        final List<String> actualLines = IOUtils.readLines(new StringReader(sb.toString()));

        // Assert
        assertEquals(expectedLines, actualLines);
    }

    @Test
    @DisplayName("appendParagraphFormat() should do nothing for an empty format string")
    void appendParagraphFormat_shouldHandleEmptyString() throws IOException {
        // Arrange
        final String emptyFormat = "";

        // Act
        underTest.appendParagraphFormat(emptyFormat);

        // Assert
        assertEquals(0, sb.length(), "Appending an empty paragraph should not write any output.");
    }
}