package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.Collections;
import java.util.List;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for text wrapping and table formatting in {@link TextHelpAppendable}.
 */
public class TextHelpAppendableTest {

    // Common data for table tests
    private static final List<String> TABLE_HEADERS = List.of("fox", "time");
    private static final List<List<String>> TABLE_ROWS = List.of(
        List.of("The quick brown fox jumps over the lazy dog", "Now is the time for all good people to come to the aid of their country"),
        List.of("Léimeann an sionnach donn gasta thar an madra leisciúil", "Anois an t-am do na daoine maithe go léir teacht i gcabhair ar a dtír")
    );

    private StringBuilder sb;
    private TextHelpAppendable underTest;

    @BeforeEach
    void setUp() {
        sb = new StringBuilder();
        underTest = new TextHelpAppendable(sb);
    }

    @ParameterizedTest
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    void indexOfWrap_shouldFindCorrectPosition_basedOnWhitespace(final Character c, final boolean isWhitespace) {
        // This test verifies the behavior of indexOfWrap.
        // The method searches for a wrap position within a given width (7).
        final String text = String.format("Hello%cWorld", c);
        final int width = 7;
        final int startPos = 0;

        // For the string "Hello<c>World" and a width of 7:
        // Indices:         01234 5 67890
        // - If 'c' is whitespace, the last whitespace before index 7 is at index 5.
        // - If 'c' is not whitespace, the test expects 6, which is the position right before the width limit (width - 1).
        final int expectedPosition = isWhitespace ? 5 : 6;

        final int actualPosition = TextHelpAppendable.indexOfWrap(text, width, startPos);

        assertEquals(expectedPosition, actualPosition);
    }

    @Test
    void appendTable_withTitle_shouldRenderFullTable() throws IOException {
        // Arrange
        final List<TextStyle> styles = createColumnStyles();
        final TableDefinition table = TableDefinition.from("Common Phrases", styles, TABLE_HEADERS, TABLE_ROWS);
        underTest.setMaxWidth(80);

        // Using a text block for readability. The expected output is formatted precisely.
        // Note: Trailing spaces are significant in this test's output.
        final String expectedOutput = String.join(System.lineSeparator(),
            " Common Phrases",
            "",
            "               fox                                       time                   ",
            " The quick brown fox jumps over           Now is the time for all good people to",
            "   the lazy dog                                 come to the aid of their country",
            " Léimeann an sionnach donn gasta       Anois an t-am do na daoine maithe go léir",
            "   thar an madra leisciúil                           teacht i gcabhair ar a dtír",
            ""
        );

        // Act
        underTest.appendTable(table);
        final String actualOutput = sb.toString();

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void appendTable_withoutTitle_shouldRenderTableWithoutTitle() throws IOException {
        // Arrange
        final List<TextStyle> styles = createColumnStyles();
        final TableDefinition table = TableDefinition.from(null, styles, TABLE_HEADERS, TABLE_ROWS);
        underTest.setMaxWidth(80);

        final String expectedOutput = String.join(System.lineSeparator(),
            "               fox                                       time                   ",
            " The quick brown fox jumps over           Now is the time for all good people to",
            "   the lazy dog                                 come to the aid of their country",
            " Léimeann an sionnach donn gasta       Anois an t-am do na daoine maithe go léir",
            "   thar an madra leisciúil                           teacht i gcabhair ar a dtír",
            ""
        );

        // Act
        underTest.appendTable(table);
        final String actualOutput = sb.toString();

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    @Test
    void appendTable_withNoRows_shouldRenderOnlyHeaders() throws IOException {
        // Arrange
        final List<TextStyle> styles = createColumnStyles();
        final TableDefinition table = TableDefinition.from(null, styles, TABLE_HEADERS, Collections.emptyList());
        underTest.setMaxWidth(80);

        final String expectedOutput = String.join(System.lineSeparator(),
            " fox     time",
            ""
        );

        // Act
        underTest.appendTable(table);
        final String actualOutput = sb.toString();

        // Assert
        assertEquals(expectedOutput, actualOutput);
    }

    private List<TextStyle> createColumnStyles() {
        final TextStyle.Builder styleBuilder = TextStyle.builder();
        return List.of(
            styleBuilder.setIndent(2).get(),
            styleBuilder.setIndent(0).setLeftPad(5).setAlignment(TextStyle.Alignment.RIGHT).get()
        );
    }
}