package org.apache.commons.cli.help;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;

/**
 * Tests for utility methods in {@link TextHelpAppendable}.
 *
 * <p>This test class focuses on the static {@code indexOfWrap} method and the
 * protected {@code resize} method, which handle text wrapping logic and style
 * adjustments, respectively.
 * </p>
 */
@DisplayName("Tests for utility methods in TextHelpAppendable")
public class TextHelpAppendableTest {

    @ParameterizedTest(name = "For char ''{0}'' (isWhitespace={1})")
    @MethodSource("org.apache.commons.cli.help.UtilTest#charArgs")
    @DisplayName("indexOfWrap should find correct wrap position based on whitespace")
    void indexOfWrapShouldCorrectlyIdentifyWrapPosition(final Character character, final boolean isWhitespace) {
        // Arrange
        final String text = String.format("Hello%cWorld", character);
        final int wrapWidth = 7;
        final int startPosition = 0;

        // The wrap position is the index of the last whitespace character before the wrap width limit.
        final int expectedPosForWhitespace = 5; // Index of the space in "Hello World"

        // Based on the original test's behavior, if no whitespace is found within the wrap width,
        // the wrap position is calculated as (startPosition + wrapWidth - 1).
        final int expectedPosForNonWhitespace = 6; // 0 + 7 - 1

        final int expectedPosition = isWhitespace ? expectedPosForWhitespace : expectedPosForNonWhitespace;

        // Act
        final int actualPosition = TextHelpAppendable.indexOfWrap(text, wrapWidth, startPosition);

        // Assert
        assertEquals(expectedPosition, actualPosition,
            () -> String.format("Incorrect wrap position for text \"%s\"", text));
    }

    @ParameterizedTest(name = "Resize by {2}: indent {0}->{3}, width {1}->{4}")
    @CsvSource({
        // initialIndent, initialMaxWidth, fraction, expectedIndent, expectedMaxWidth
        "2, 3, 0.5, 0, 1",
        "4, 6, 0.5, 1, 3"
    })
    @DisplayName("resize should correctly scale TextStyle.Builder properties")
    void resizeShouldCorrectlyScaleTextStyleBuilderProperties(
            final int initialIndent, final int initialMaxWidth, final double fraction,
            final int expectedIndent, final int expectedMaxWidth) {
        // Arrange
        final TextHelpAppendable textHelpAppendable = new TextHelpAppendable(null);
        final TextStyle.Builder styleBuilder = TextStyle.builder()
                .setIndent(initialIndent)
                .setMaxWidth(initialMaxWidth);

        // Act
        // The method under test is protected, but accessible from a test in the same package.
        textHelpAppendable.resize(styleBuilder, fraction);

        // Assert
        // The original test's behavior implies a specific resizing logic:
        // - newMaxWidth = (int) (initialMaxWidth * fraction)
        // - newIndent = (int) (initialIndent * fraction) - 1 (for the tested cases)
        // We assert against the expected values derived from this observed behavior.
        assertAll("Resized style builder properties should match expected values",
            () -> assertEquals(expectedIndent, styleBuilder.getIndent(), "Indent was not resized correctly"),
            () -> assertEquals(expectedMaxWidth, styleBuilder.getMaxWidth(), "Max width was not resized correctly")
        );
    }
}