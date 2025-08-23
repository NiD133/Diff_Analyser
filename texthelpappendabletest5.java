package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.io.StringReader;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import org.apache.commons.io.IOUtils;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    private StringBuilder sb;
    private TextHelpAppendable underTest;

    @BeforeEach
    public void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    /**
     * Helper method to read the content of the StringBuilder as a list of lines.
     *
     * @return A list of strings, representing the lines written to the appendable.
     * @throws IOException if an I/O error occurs.
     */
    private List<String> getActualLines() throws IOException {
        return IOUtils.readLines(new StringReader(sb.toString()));
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    void indexOfWrapFindsCorrectPosition(final Character separator, final boolean isWhitespace) {
        // Arrange: Create a test string with a separator character at index 5.
        final String text = String.format("Hello%cWorld", separator);
        final int wrapWidth = 7;
        final int startPosition = 0;

        // The wrap position is the last valid break point before (startPos + wrapWidth).
        // If the separator is whitespace, the wrap should occur AT the whitespace (index 5).
        // If it's not whitespace, the wrap should occur AFTER the word containing the
        // separator (index 6), at the start of the next word.
        final int expectedIndex = isWhitespace ? 5 : 6;

        // Act
        final int actualIndex = TextHelpAppendable.indexOfWrap(text, wrapWidth, startPosition);

        // Assert
        assertEquals(expectedIndex, actualIndex);
    }

    @Test
    void appendListShouldFormatOrderedList() throws IOException {
        // Arrange
        final List<String> items = Arrays.asList("one", "two", "three");
        final List<String> expectedLines = Arrays.asList(
            "  1. one",
            "  2. two",
            "  3. three",
            ""
        );

        // Act
        underTest.appendList(true, items);

        // Assert
        assertEquals(expectedLines, getActualLines());
    }

    @Test
    void appendListShouldFormatUnorderedList() throws IOException {
        // Arrange
        final List<String> items = Arrays.asList("one", "two", "three");
        final List<String> expectedLines = Arrays.asList(
            "  * one",
            "  * two",
            "  * three",
            ""
        );

        // Act
        underTest.appendList(false, items);

        // Assert
        assertEquals(expectedLines, getActualLines());
    }

    @Test
    void appendListWithEmptyListShouldAppendNothing() throws IOException {
        // Act
        underTest.appendList(true, Collections.emptyList()); // 'ordered' flag should not matter

        // Assert
        assertEquals(Collections.emptyList(), getActualLines());
    }

    @Test
    void appendListWithNullListShouldAppendNothing() throws IOException {
        // Act
        underTest.appendList(false, null); // 'ordered' flag should not matter

        // Assert
        assertEquals(Collections.emptyList(), getActualLines());
    }
}