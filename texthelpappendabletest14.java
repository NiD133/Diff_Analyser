package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

/**
 * Tests for the TextHelpAppendable class, focusing on text formatting and layout logic.
 */
class TextHelpAppendableTest {

    /**
     * Tests that indexOfWrap correctly identifies the position of a whitespace character
     * when it falls within the specified wrapping width.
     */
    @Test
    void indexOfWrapShouldReturnPositionOfWhitespace() {
        // Given a string with a space character within the wrap width limit
        final String textWithSpace = "Hello World"; // Space is at index 5
        final int wrapWidth = 7;
        final int startPosition = 0;

        // When searching for the wrap position
        final int wrapIndex = TextHelpAppendable.indexOfWrap(textWithSpace, wrapWidth, startPosition);

        // Then the returned index should be the position of the space character
        assertEquals(5, wrapIndex, "Wrap index should be at the space character");
    }

    /**
     * Tests a specific behavior of indexOfWrap where, in the absence of whitespace within
     * the wrap width, it returns a position based on the width limit. The original test
     * showed that for "HelloXWorld" with a width of 7, the expected index is 6.
     */
    @Test
    void indexOfWrapShouldReturnCalculatedPositionWhenNoWhitespaceFound() {
        // Given a string with no whitespace within the wrap width limit
        final String textWithoutSpace = "HelloXWorld";
        final int wrapWidth = 7;
        final int startPosition = 0;

        // When searching for the wrap position
        final int wrapIndex = TextHelpAppendable.indexOfWrap(textWithoutSpace, wrapWidth, startPosition);

        // Then the returned index should be 6, as determined by the method's logic
        // for handling lines without a natural break.
        assertEquals(6, wrapIndex, "Wrap index should be 6 for 'HelloXWorld' with width 7");
    }

    /**
     * Verifies that adjustTableFormat does not alter the minimum or maximum widths of
     * table columns when the table's total width already fits within the configured
     * maximum page width.
     */
    @Test
    void adjustTableFormatShouldNotChangeWidthsWhenTableFitsWithinMaxWidth() {
        // Given a TextHelpAppendable with a large maximum width
        final TextHelpAppendable helpFormatter = new TextHelpAppendable(new StringBuilder());
        helpFormatter.setMaxWidth(150);

        // And a table definition that fits comfortably within that width
        final TextStyle columnStyle = TextStyle.builder().setMinWidth(20).setMaxWidth(100).get();
        final List<TextStyle> columnStyles = Collections.singletonList(columnStyle);
        final List<String> headers = Collections.singletonList("header");
        final List<List<String>> rows = Collections.singletonList(Collections.singletonList("one"));
        final TableDefinition tableDefinition = TableDefinition.from("Caption", columnStyles, headers, rows);

        // When the table format is adjusted
        final TableDefinition adjustedTable = helpFormatter.adjustTableFormat(tableDefinition);

        // Then the original column widths should be preserved
        final TextStyle adjustedStyle = adjustedTable.columnTextStyles().get(0);
        assertEquals(20, adjustedStyle.getMinWidth(), "Minimum width should be preserved");
        assertEquals(100, adjustedStyle.getMaxWidth(), "Maximum width should be preserved");
    }
}