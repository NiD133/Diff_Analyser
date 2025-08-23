package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TextHelpAppendable}.
 * This suite focuses on text wrapping and paragraph appending logic.
 */
@DisplayName("TextHelpAppendable Tests")
class TextHelpAppendableTest {

    private StringBuilder outputBuffer;
    private TextHelpAppendable textHelp;

    @BeforeEach
    void setUp() {
        outputBuffer = new StringBuilder();
        textHelp = new TextHelpAppendable(outputBuffer);
    }

    @DisplayName("indexOfWrap() should find the correct wrapping position")
    @ParameterizedTest(name = "[{index}] For char ''{0}'' (isWhitespace={1})")
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    void indexOfWrap_findsCorrectWrapPosition(final Character character, final boolean isWhitespace) {
        // Arrange
        final String text = String.format("Hello%cWorld", character);
        final int searchWidth = 7;
        final int startPosition = 0;

        // This test asserts a specific wrapping behavior:
        // 1. If the character at index 5 is whitespace, the wrap position is at that character (index 5).
        // 2. If there's no whitespace within the search width, it wraps at the position just before
        //    the width limit (searchWidth - 1 = 6). This prevents lines from exceeding the max width.
        final int expectedPosition = isWhitespace ? 5 : 6;

        // Act
        final int actualPosition = TextHelpAppendable.indexOfWrap(text, searchWidth, startPosition);

        // Assert
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    @DisplayName("appendParagraph() with text should append indented text and a blank line")
    void appendParagraph_withText_appendsIndentedTextAndBlankLine() throws IOException {
        // Arrange
        final String paragraph = "Hello World";
        // Expects default left padding (1 space) and a blank line after the paragraph.
        final List<String> expectedLines = Arrays.asList(" " + paragraph, "");

        // Act
        textHelp.appendParagraph(paragraph);
        final List<String> actualLines = IOUtils.readLines(new StringReader(outputBuffer.toString()));

        // Assert
        assertEquals(expectedLines, actualLines);
    }

    @Test
    @DisplayName("appendParagraph() with an empty string should not append anything")
    void appendParagraph_withEmptyString_doesNotAppendAnything() throws IOException {
        // Act
        textHelp.appendParagraph("");

        // Assert
        assertEquals("", outputBuffer.toString(), "Appending an empty paragraph should result in no output.");
    }

    @Test
    @DisplayName("appendParagraph() with null should not append anything")
    void appendParagraph_withNull_doesNotAppendAnything() throws IOException {
        // Act
        textHelp.appendParagraph(null);

        // Assert
        assertEquals("", outputBuffer.toString(), "Appending a null paragraph should result in no output.");
    }
}