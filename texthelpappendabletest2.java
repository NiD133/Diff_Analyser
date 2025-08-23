package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collections;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link TextHelpAppendable}.
 */
class TextHelpAppendableTest {

    private TextHelpAppendable underTest;

    @BeforeEach
    void setUp() {
        // The StringBuilder is not used in these tests, so it's omitted.
        // If other tests need it, it can be added back.
        underTest = new TextHelpAppendable(new StringBuilder());
    }

    // --- Tests for indexOfWrap ---

    @Test
    void indexOfWrap_whenWhitespaceExistsBeforeWidthLimit_returnsWhitespacePosition() {
        // Arrange
        // Text with a space at index 5. The wrap search width is 7.
        final String textWithWhitespace = "Hello World";
        final int searchWidth = 7;
        final int startPosition = 0;
        final int expectedWrapIndex = 5; // The index of the space character

        // Act
        final int actualWrapIndex = TextHelpAppendable.indexOfWrap(textWithWhitespace, searchWidth, startPosition);

        // Assert
        assertEquals(expectedWrapIndex, actualWrapIndex,
            "Should return the index of the last whitespace character before the width limit.");
    }

    @Test
    void indexOfWrap_whenNoWhitespaceExistsBeforeWidthLimit_returnsPositionAtWidthBoundary() {
        // Arrange
        // Text with no space. The wrap search width is 7.
        final String textWithoutWhitespace = "HelloWorld";
        final int searchWidth = 7;
        final int startPosition = 0;
        // The original test expected 6 for a width of 7. This is the position
        // right before the character that would exceed the width limit.
        final int expectedWrapIndex = 6;

        // Act
        final int actualWrapIndex = TextHelpAppendable.indexOfWrap(textWithoutWhitespace, searchWidth, startPosition);

        // Assert
        assertEquals(expectedWrapIndex, actualWrapIndex,
            "Should return the position just before the width limit when no whitespace is found.");
    }

    // --- Tests for adjustTableFormat ---

    @Test
    void adjustTableFormat_whenColumnWidthIsSmallerThanHeader_adjustsWidthToHeaderLength() {
        // Arrange
        final String headerText = "header";
        final int configuredMaxWidth = 3; // Intentionally smaller than the header's length

        // Create a table where the column's configured max width is less than its header's length.
        // @formatter:off
        final TableDefinition initialTableDefinition = TableDefinition.from(
            "Testing",
            Collections.singletonList(TextStyle.builder().setMaxWidth(configuredMaxWidth).get()),
            Collections.singletonList(headerText),
            Collections.singletonList(Collections.singletonList("data"))
        );
        // @formatter:on

        // Act
        // The method under test should adjust the column width to fit the header.
        final TableDefinition adjustedTableDefinition = underTest.adjustTableFormat(initialTableDefinition);

        // Assert
        final TextStyle adjustedStyle = adjustedTableDefinition.columnTextStyles().get(0);
        final int expectedWidth = headerText.length();

        assertEquals(expectedWidth, adjustedStyle.getMaxWidth(),
            "Max width should be adjusted to the header's length.");
        assertEquals(expectedWidth, adjustedStyle.getMinWidth(),
            "Min width should also be adjusted to the header's length.");
    }
}