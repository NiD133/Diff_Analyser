package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for {@link TextHelpAppendable}.
 *
 * This version removes shared state (instance fields and @BeforeEach) to make
 * each test self-contained and easier to understand independently.
 */
// Renamed from TextHelpAppendableTestTest10 for clarity and conciseness.
public class TextHelpAppendableTest {

    /**
     * Tests that {@link TextHelpAppendable#indexOfWrap(CharSequence, int, int)} correctly
     * identifies a wrap position based on the presence of a whitespace character within the
     * search width.
     *
     * @param c a character to insert into the test string
     * @param isWhitespace true if the character is whitespace, false otherwise
     */
    @ParameterizedTest
    // The method name now clearly describes the behavior under test.
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    void indexOfWrapShouldIdentifyPositionBasedOnWhitespace(final Character c, final boolean isWhitespace) {
        // Arrange: Create a test string where a specific character is at index 5.
        final String text = String.format("Hello%cWorld", c);
        final int searchWidth = 7;
        final int startPosition = 0;

        // The test verifies the wrap position within the first 7 characters ("Hello<c>W").
        // If 'c' is whitespace, the wrap position should be its index (5).
        // Otherwise, with no earlier whitespace, the wrap position is the next index (6).
        final int expectedWrapIndex = isWhitespace ? 5 : 6;

        // Act: Find the wrap index.
        final int actualWrapIndex = TextHelpAppendable.indexOfWrap(text, searchWidth, startPosition);

        // Assert: Check if the calculated wrap index is correct.
        assertEquals(expectedWrapIndex, actualWrapIndex);
    }

    /**
     * Tests that {@link TextHelpAppendable#getTextStyleBuilder()} returns a builder
     * configured with the default values from the class constants.
     */
    @Test
    // The method name is more descriptive of the test's purpose.
    void getStyleBuilderShouldReturnBuilderWithDefaultValues() {
        // Arrange: Create a new instance of the class under test.
        final TextHelpAppendable helpAppendable = new TextHelpAppendable(new StringBuilder());

        // Act: Get the style builder.
        final TextStyle.Builder builder = helpAppendable.getTextStyleBuilder();

        // Assert: Verify that the builder's properties match the default constants.
        // Using assertAll to group related checks and report all failures at once.
        assertAll("Default text style builder properties",
            () -> assertEquals(TextHelpAppendable.DEFAULT_WIDTH, builder.getMaxWidth(), "max width should match default"),
            () -> assertEquals(TextHelpAppendable.DEFAULT_LEFT_PAD, builder.getLeftPad(), "left pad should match default"),
            () -> assertEquals(TextHelpAppendable.DEFAULT_INDENT, builder.getIndent(), "indent should match default")
        );
    }
}