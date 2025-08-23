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
 * Tests for the text wrapping and formatting capabilities of {@link TextHelpAppendable}.
 */
@DisplayName("TextHelpAppendable Tests")
class TextHelpAppendableTest {

    private static final String LIPSUM = "The quick brown fox jumps over the lazy dog";

    private StringBuilder sb;
    private TextHelpAppendable underTest;

    @BeforeEach
    void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    @Test
    @DisplayName("Default constructor should set default formatting values")
    void constructor_ShouldSetDefaultValues() {
        assertEquals(TextHelpAppendable.DEFAULT_WIDTH, underTest.getMaxWidth(), "Default width should be set correctly");
        assertEquals(TextHelpAppendable.DEFAULT_LEFT_PAD, underTest.getLeftPad(), "Default left pad should be set correctly");
        assertEquals(TextHelpAppendable.DEFAULT_INDENT, underTest.getIndent(), "Default indent should be set correctly");
    }

    @ParameterizedTest(name = "For char ''{0}'', whitespace={1}")
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    @DisplayName("indexOfWrap() should find correct wrap position")
    void indexOfWrap_ShouldFindCorrectPosition_ForWhitespaceAndNonWhitespace(final Character c, final boolean isWhitespace) {
        final String text = String.format("Hello%cWorld", c);
        // If the character is whitespace, the wrap should occur at its position (5).
        // Otherwise, it should occur at the next character's position (6).
        final int expectedWrapPos = isWhitespace ? 5 : 6;
        assertEquals(expectedWrapPos, TextHelpAppendable.indexOfWrap(text, 7, 0));
    }

    @Test
    @DisplayName("printWrapped() should wrap and align text to the left")
    void printWrapped_WithLeftAlignment_ShouldWrapAndAlignTextLeft() throws IOException {
        // Arrange
        final TextStyle style = TextStyle.builder().setMaxWidth(10).build();
        final List<String> expected = List.of(
            "The quick",
            "brown fox",
            "jumps over",
            "the lazy",
            "dog"
        );

        // Act
        underTest.printWrapped(LIPSUM, style);
        final List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("printWrapped() should wrap and align text to the right")
    void printWrapped_WithRightAlignment_ShouldWrapAndAlignTextRight() throws IOException {
        // Arrange
        final TextStyle style = TextStyle.builder().setMaxWidth(10).setAlignment(TextStyle.Alignment.RIGHT).build();
        final List<String> expected = List.of(
            " The quick",
            " brown fox",
            "jumps over",
            "  the lazy",
            "       dog"
        );

        // Act
        underTest.printWrapped(LIPSUM, style);
        final List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("printWrapped() should wrap and align text to the center")
    void printWrapped_WithCenterAlignment_ShouldWrapAndAlignCenter() throws IOException {
        // Arrange
        final TextStyle style = TextStyle.builder().setMaxWidth(10).setAlignment(TextStyle.Alignment.CENTER).build();
        final List<String> expected = List.of(
            "The quick",
            "brown fox",
            "jumps over",
            " the lazy",
            "   dog"
        );

        // Act
        underTest.printWrapped(LIPSUM, style);
        final List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("printWrapped() without style should use default formatting")
    void printWrapped_WithDefaultStyle_ShouldUseDefaultFormatting() throws IOException {
        // Arrange
        final List<String> expected = List.of(
            " The quick brown fox jumps over the lazy dog"
        );

        // Act
        underTest.printWrapped(LIPSUM);
        final List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));

        // Assert
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("printWrapped() with default style should handle newlines correctly")
    void printWrapped_WithDefaultStyleAndNewlines_ShouldWrapAndIndentCorrectly() throws IOException {
        // Arrange
        final String textWithNewline = LIPSUM + ".\nNow is the time for all good people to come to the aid of their country.";
        final List<String> expected = List.of(
            " The quick brown fox jumps over the lazy dog.",
            "    Now is the time for all good people to come to the aid of their",
            "    country."
        );

        // Act
        underTest.printWrapped(textWithNewline);
        final List<String> actual = IOUtils.readLines(new StringReader(sb.toString()));

        // Assert
        assertEquals(expected, actual);
    }
}