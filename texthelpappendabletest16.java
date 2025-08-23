package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

/**
 * Tests for {@link TextHelpAppendable} focusing on text wrapping and column formatting.
 */
@DisplayName("TextHelpAppendable Tests")
class TextHelpAppendableTest {

    private StringBuilder sb;
    private TextHelpAppendable underTest;

    @BeforeEach
    void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    @DisplayName("indexOfWrap() should find correct wrap position based on whitespace")
    @ParameterizedTest(name = "For char ''{0}'', expected position is {2}")
    @CsvSource({
        // Character, isWhitespace, expectedPosition
        "' ',         true,         5",    // Space is a wrap point
        "'\t',        true,         5",    // Tab is a wrap point
        "'X',         false,        6",    // Non-whitespace
        "'a',         false,        6"     // Non-whitespace
    })
    void indexOfWrap_shouldCorrectlyIdentifyWrapPosition(final char testChar, final boolean isWhitespace, final int expectedPosition) {
        // Arrange
        final String text = String.format("Hello%cWorld", testChar);
        final int width = 7;
        final int startPos = 0;

        // This test verifies the wrapping logic.
        // If the character at index 5 is whitespace, the line should wrap at that position (5).
        // If it's not whitespace, the word "Hello[c]World" is treated as a single token.
        // The implementation returns 6, which is the start of the part of the token
        // that overflows the line width.
        assertEquals(expectedPosition, isWhitespace ? 5 : 6, "Test data sanity check failed.");

        // Act
        final int actualPosition = TextHelpAppendable.indexOfWrap(text, width, startPos);

        // Assert
        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    @DisplayName("writeColumnQueues() should format and align multiple columns with padding")
    void writeColumnQueues_shouldFormatAndAlignMultipleColumns() throws IOException {
        // Arrange
        // Define two columns of text, with the second being longer than the first.
        final Queue<String> column1Data = new LinkedList<>(List.of(
            "The quick ", "brown fox ", "jumps over", "the lazy  ", "dog       "
        ));
        final Queue<String> column2Data = new LinkedList<>(List.of(
            "     Now is the", "     time for  ", "     all good  ", "     people to ",
            "     come to   ", "     the aid of", "     their     ", "     country   "
        ));
        final List<Queue<String>> columnQueues = List.of(column1Data, column2Data);

        // Define styles for each column.
        final TextStyle style1 = TextStyle.builder().setMaxWidth(10).setLeftPad(0).get();
        final TextStyle style2 = TextStyle.builder().setMaxWidth(10).setLeftPad(5).get();
        final List<TextStyle> columnStyles = List.of(style1, style2);

        // The expected output is a formatted table where columns are aligned and padded.
        // The first column will have blank lines to match the length of the second.
        final List<String> expectedLines = Arrays.asList(
            " The quick      Now is the",
            " brown fox      time for  ",
            " jumps over     all good  ",
            " the lazy       people to ",
            " dog            come to   ",
            "                the aid of",
            "                their     ",
            "                country   "
        );

        // Act
        underTest.writeColumnQueues(columnQueues, columnStyles);
        final List<String> actualLines = IOUtils.readLines(new StringReader(sb.toString()));

        // Assert
        assertEquals(expectedLines, actualLines);
    }
}